package per.goweii.wanandroid.module.knowledge.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.kennyc.view.MultiStateView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.List;

import per.goweii.actionbarex.common.ActionBarCommon;
import per.goweii.basic.core.adapter.MultiFragmentPagerAdapter;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.common.Config;
import per.goweii.wanandroid.databinding.ActivityKnowledgeArticleBinding;
import per.goweii.wanandroid.event.ScrollTopEvent;
import per.goweii.wanandroid.module.knowledge.fragment.KnowledgeArticleFragment;
import per.goweii.wanandroid.module.knowledge.presenter.KnowledgePresenter;
import per.goweii.wanandroid.module.knowledge.view.KnowledgeView;
import per.goweii.wanandroid.module.main.model.ArticleBean;
import per.goweii.wanandroid.module.main.model.ChapterBean;
import per.goweii.wanandroid.utils.MagicIndicatorUtils;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.router.Router;

/**
 * @author CuiZhen
 * @date 2019/5/12
 * GitHub: https://github.com/goweii
 */
public class KnowledgeArticleActivity extends BaseActivity<KnowledgePresenter,KnowledgeView> implements KnowledgeView {

    ActionBarCommon abc;
    MultiStateView msv;
    MagicIndicator mi;
    ViewPager vp;

    private long lastClickTime = 0L;
    private int lastClickPos = 0;
    private MultiFragmentPagerAdapter<ChapterBean, KnowledgeArticleFragment> mAdapter;
    private CommonNavigator mCommonNavigator;
    private int mSuperChapterId;
    private int mChapterId;

    public static void start(Context context, ChapterBean chapterBean, int currPos) {
        Intent intent = new Intent(context, KnowledgeArticleActivity.class);
        intent.putExtra("chapterBean", chapterBean);
        intent.putExtra("currPos", currPos);
        context.startActivity(intent);
    }

    public static void start(Context context, int superChapterId, String superChapterName, int chapterId) {
        Intent intent = new Intent(context, KnowledgeArticleActivity.class);
        intent.putExtra("superChapterName", superChapterName);
        intent.putExtra("superChapterId", superChapterId);
        intent.putExtra("chapterId", chapterId);
        context.startActivity(intent);
    }

    public static void start(Context context, ArticleBean.TagsBean tag) {
        if (tag == null) return;
        String url = tag.getUrl();
        if (TextUtils.isEmpty(url)) return;
        // /wxarticle/list/410/1
        // /article/list/0?cid=440
        // /project/list/1?cid=367
        // wana://www.wanandroid.com/wxarticle/list/410/1?cid=440
        Uri uri = Uri.parse(Router.createUrlByPath(url));
        try {
            String cid = uri.getQueryParameter("cid");
            if (TextUtils.isEmpty(cid)) {
                List<String> paths = uri.getPathSegments();
                if (paths != null && paths.size() >= 3) {
                    cid = paths.get(2);
                }
            }
            int chapterId = 0;
            if (cid != null) {
                chapterId = Integer.parseInt(cid);
            }
            if (chapterId > 0) {
                start(context, 0, "", chapterId);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public void initContentView() {
        ActivityKnowledgeArticleBinding binding = ActivityKnowledgeArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        msv = binding.msv;
        mi = binding.mi;
        vp = binding.vp;
    }


    @Override
    protected void setUpPresenter() {
        presenter =  new KnowledgePresenter();
    }

    @Override
    protected void initViews() {
        abc.getTitleTextView().setOnClickListener(v -> notifyScrollTop(vp.getCurrentItem()));
        mAdapter = new MultiFragmentPagerAdapter<>(
                getSupportFragmentManager(),
                new MultiFragmentPagerAdapter.FragmentCreator<>() {
                    @Override
                    public KnowledgeArticleFragment create(ChapterBean data, int pos) {
                        return KnowledgeArticleFragment.create(data, pos);
                    }

                    @Override
                    public String getTitle(ChapterBean data) {
                        return data.getName();
                    }
                });
        vp.setAdapter(mAdapter);
        mCommonNavigator = MagicIndicatorUtils.commonNavigator(mi, vp, mAdapter, this::notifyScrollTop);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // ignore
    }

    @Override
    protected void bindData() {
        ChapterBean bean = (ChapterBean) getIntent().getSerializableExtra("chapterBean");
        int currPos = getIntent().getIntExtra("currPos", 0);
        if (bean != null) {
            initVP(bean, currPos);
        } else {
            MultiStateUtils.toLoading(msv);
            mSuperChapterId = getIntent().getIntExtra("superChapterId", -1);
            mChapterId = getIntent().getIntExtra("chapterId", -1);
            if (mSuperChapterId <= 0 && mChapterId <= 0) {
                MultiStateUtils.toError(msv);
            } else {
                String superChapterName = getIntent().getStringExtra("superChapterName");
                abc.getTitleTextView().setText(superChapterName);
                presenter.getKnowledgeListCacheAndNet();
            }
        }
    }

    private void notifyScrollTop(int pos) {
        long currClickTime = System.currentTimeMillis();
        if (lastClickPos == pos && currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
            new ScrollTopEvent(KnowledgeArticleFragment.class, vp.getCurrentItem()).post();
        }
        lastClickPos = pos;
        lastClickTime = currClickTime;
    }

    private void initVP(ChapterBean bean, int currPos) {
        MultiStateUtils.toContent(msv);
        abc.getTitleTextView().setText(bean.getName());
        mAdapter.setDataList(bean.getChildren());
        mCommonNavigator.notifyDataSetChanged();
        vp.setCurrentItem(currPos);
    }

    @Override
    public void getKnowledgeListSuccess(int code, List<ChapterBean> data) {
        ChapterBean superChapterBean = null;
        for (ChapterBean bean : data) {
            if (bean.getId() == mSuperChapterId) {
                superChapterBean = bean;
                break;
            }
        }
        int currPos = 0;
        if (superChapterBean == null) {
            for (ChapterBean scb : data) {
                List<ChapterBean> chapters = scb.getChildren();
                for (int i = 0; i < chapters.size(); i++) {
                    ChapterBean cb = chapters.get(i);
                    if (cb.getId() == mChapterId) {
                        superChapterBean = scb;
                        currPos = i;
                        break;
                    }
                }
            }
        } else {
            List<ChapterBean> chapters = superChapterBean.getChildren();
            for (int i = 0; i < chapters.size(); i++) {
                ChapterBean cb = chapters.get(i);
                if (cb.getId() == mChapterId) {
                    currPos = i;
                    break;
                }
            }
        }
        if (superChapterBean != null) {
            initVP(superChapterBean, currPos);
        } else {
            MultiStateUtils.toError(msv);
        }
    }

    @Override
    public void getKnowledgeListFail(int code, String msg) {
        MultiStateUtils.toError(msv);
    }
}
