package per.goweii.wanandroid.module.main.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import per.goweii.actionbarex.common.ActionBarCommon;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.InputMethodUtils;
import per.goweii.basic.utils.LogUtils;
import per.goweii.basic.utils.listener.OnClickListener2;
import per.goweii.basic.utils.listener.SimpleTextWatcher;
import per.goweii.rxhttp.request.base.BaseBean;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.ActivityShareArticleBinding;
import per.goweii.wanandroid.module.main.presenter.ShareArticlePresenter;
import per.goweii.wanandroid.module.main.view.ShareArticleView;
import per.goweii.wanandroid.utils.UrlOpenUtils;
import per.goweii.wanandroid.utils.UserUtils;
import per.goweii.wanandroid.utils.web.WebHolder;
import per.goweii.wanandroid.utils.web.view.WebContainer;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class ShareArticleActivity extends BaseActivity<ShareArticlePresenter,ShareArticleView> implements ShareArticleView {

    private static final String TAG = ShareArticleActivity.class.getSimpleName();

    ActionBarCommon abc;
    WebContainer wc;
    EditText et_title;
    EditText et_link;
    TextView tv_share;

    private WebHolder mWebHolder;
    private ActivityShareArticleBinding binding;

    public static void start(Context context) {
        start(context, "");
    }

    public static void start(Context context, String link) {
        start(context, "", link);
    }

    public static void start(Context context, String title, String link) {
        if (!UserUtils.getInstance().doIfLogin(context)) {
            return;
        }
        Intent intent = new Intent(context, ShareArticleActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("link", link);
        context.startActivity(intent);
    }

    @Override
    public void initBinding() {
        binding = ActivityShareArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        wc = binding.wc;
        et_title = binding.etTitle;
        et_link = binding.etLink;
        tv_share = binding.tvShare;
        binding.tvOpen.setOnClickListener(this);
        binding.tvRefresh.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_article;
    }

    @Nullable
    @Override
    protected ShareArticlePresenter initPresenter() {
        return new ShareArticlePresenter();
    }

    @Override
    protected void initViews() {
        tv_share.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                String title = et_title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    et_title.requestFocus();
                    return;
                }
                String link = et_link.getText().toString();
                if (TextUtils.isEmpty(link)) {
                    et_link.requestFocus();
                    return;
                }
                InputMethodUtils.hide(et_link);
                presenter.shareArticle(title, link);
            }
        });
        String link = getIntent().getStringExtra("link");
        if (!TextUtils.isEmpty(link)) {
            et_link.setText(link);
        }
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            refreshTitle(link);
        } else {
            resetTitle(title);
        }
        et_link.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                refreshTitle(s.toString());
            }
        });
    }

    @Override
    protected void bindData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWebHolder != null) {
            mWebHolder.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWebHolder != null) {
            mWebHolder.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebHolder != null) {
            mWebHolder.onDestroy(true);
        }
    }

    private boolean isCorrectUrl(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        if (uri == null) return false;
        String scheme = uri.getScheme();
        if (!TextUtils.equals(scheme, "https") && !TextUtils.equals(scheme, "http")) return false;
        return !TextUtils.isEmpty(uri.getHost());
    }

    private void refreshTitle(String url) {
        LogUtils.i(TAG, "refreshTitle=" + url);
        if (mWebHolder != null) {
            mWebHolder.stopLoading();
        }
        if (!isCorrectUrl(url)) {
            resetTitle("");
            return;
        }
        if (mWebHolder == null) {
            mWebHolder = WebHolder.with(this, wc)
                    .setOnPageTitleCallback(title -> resetTitle(title))
                    .loadUrl(url);
        } else {
            mWebHolder.loadUrl(url);
        }
    }

    private void resetTitle(String title) {
        if (et_title == null) {
            return;
        }
        if (TextUtils.equals(et_title.getText().toString(), title)) {
            return;
        }
        et_title.setText(title);
        et_title.setSelection(title.length());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onClickSpace(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_refresh:
                refreshTitle(et_link.getText().toString());
                break;
            case R.id.tv_open:
                UrlOpenUtils.Companion
                        .with(et_link.getText().toString())
                        .title(et_title.getText().toString())
                        .open(getContext());
                break;
        }
    }

    @Override
    public void shareArticleSuccess(int code, BaseBean data) {
        finish();
    }

    @Override
    public void shareArticleFailed(int code, String msg) {
        ToastMaker.showShort(msg);
    }
}
