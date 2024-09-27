package per.goweii.wanandroid.module.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import per.goweii.actionbarex.ActionBarEx;
import per.goweii.basic.core.adapter.FixedFragmentPagerAdapter;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.core.mvp.IPresenter;
import per.goweii.basic.utils.listener.SimpleCallback;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.common.Config;
import per.goweii.wanandroid.databinding.ActivityHostInterruptBinding;
import per.goweii.wanandroid.module.mine.fragment.HostBlackFragment;
import per.goweii.wanandroid.module.mine.fragment.HostWhiteFragment;
import per.goweii.wanandroid.utils.MagicIndicatorUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class HostInterruptActivity extends BaseActivity {

    //@BindView(R.id.ab)
    ActionBarEx ab;
    //@BindView(R.id.vp)
    ViewPager vp;
    private FixedFragmentPagerAdapter mAdapter;
    private long lastClickTime = 0L;
    private int lastClickPos = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, HostInterruptActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initBinding() {
        ActivityHostInterruptBinding binding = ActivityHostInterruptBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ab = binding.ab;
        vp = binding.vp;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_host_interrupt;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initViews() {
        ImageView ivBack = ab.getView(R.id.action_bar_fixed_magic_indicator_iv_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new FixedFragmentPagerAdapter(getSupportFragmentManager());
        mAdapter.setTitles("白名单", "黑名单");
        mAdapter.setFragmentList(
                HostWhiteFragment.create(),
                HostBlackFragment.create()
        );
        vp.setAdapter(mAdapter);
        MagicIndicatorUtils.commonNavigator(ab.getView(R.id.mi), vp, mAdapter, new SimpleCallback<Integer>() {
            @Override
            public void onResult(Integer data) {
                notifyScrollTop(data);
            }
        });
    }

    @Override
    protected void bindData() {
    }

    private void notifyScrollTop(int pos) {
        long currClickTime = System.currentTimeMillis();
        if (lastClickPos == pos && currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
            Fragment fragment = mAdapter.getItem(pos);
            if (fragment instanceof RvScrollTopUtils.ScrollTop) {
                RvScrollTopUtils.ScrollTop scrollTop = (RvScrollTopUtils.ScrollTop) fragment;
                scrollTop.scrollTop();
            }
        }
        lastClickPos = pos;
        lastClickTime = currClickTime;
    }
}
