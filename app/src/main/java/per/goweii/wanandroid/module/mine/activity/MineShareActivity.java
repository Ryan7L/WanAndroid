package per.goweii.wanandroid.module.mine.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import per.goweii.actionbarex.common.ActionBarCommon;
import per.goweii.basic.core.base.BaseActivity;
import per.goweii.basic.core.utils.SmartRefreshUtils;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.common.Config;
import per.goweii.wanandroid.databinding.ActivityMineShareBinding;
import per.goweii.wanandroid.event.ArticleDeleteEvent;
import per.goweii.wanandroid.event.ArticleShareEvent;
import per.goweii.wanandroid.event.CollectionEvent;
import per.goweii.wanandroid.module.main.activity.ShareArticleActivity;
import per.goweii.wanandroid.module.main.adapter.ArticleAdapter;
import per.goweii.wanandroid.module.main.model.ArticleBean;
import per.goweii.wanandroid.module.main.model.ArticleListBean;
import per.goweii.wanandroid.module.mine.adapter.MineShareArticleAdapter;
import per.goweii.wanandroid.module.mine.presenter.MineSharePresenter;
import per.goweii.wanandroid.module.mine.view.MineShareView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;
import per.goweii.wanandroid.widget.CollectView;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class MineShareActivity extends BaseActivity<MineSharePresenter,MineShareView> implements MineShareView {

    public static final int PAGE_START = 1;

    ActionBarCommon abc;
    MultiStateView msv;
    SmartRefreshLayout srl;
    RecyclerView rv;
    private SmartRefreshUtils mSmartRefreshUtils;
    private MineShareArticleAdapter mAdapter;
    private int currPage = PAGE_START;
    private long lastClickTime = 0L;

    public static void start(Context context) {
        Intent intent = new Intent(context, MineShareActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void initContentView() {
        ActivityMineShareBinding binding = ActivityMineShareBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        abc = binding.abc;
        msv = binding.msv;
        srl = binding.srl;
        rv = binding.rv;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionEvent(CollectionEvent event) {
        if (isDestroyed()) {
            return;
        }
        mAdapter.notifyCollectionEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArticleShareEvent(ArticleShareEvent event) {
        currPage = PAGE_START;
        presenter.getMineShareArticleList(currPage, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArticleDeleteEvent(ArticleDeleteEvent event) {
        if (event.getArticleId() <= 0) {
            currPage = PAGE_START;
            presenter.getMineShareArticleList(currPage, true);
        } else {
            mAdapter.forEach((dataPos, adapterPos, bean) -> {
                if (event.getArticleId() == bean.getId()) {
                    mAdapter.remove(adapterPos);
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void setUpPresenter() {
        presenter =  new MineSharePresenter();
    }

    @Override
    protected void initViews() {
        abc.setOnRightIconClickListener(v -> ShareArticleActivity.start(getViewContext()));
        abc.getTitleTextView().setOnClickListener(v -> {
            long currClickTime = System.currentTimeMillis();
            if (currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
                RvScrollTopUtils.smoothScrollTop(rv);
            }
            lastClickTime = currClickTime;
        });
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(() -> {
            currPage = PAGE_START;
            presenter.getMineShareArticleList(currPage, true);
        });
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new MineShareArticleAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(() -> presenter.getMineShareArticleList(currPage, true), rv);
        mAdapter.setOnCollectListener(new ArticleAdapter.OnCollectListener() {
            @Override
            public void collect(ArticleBean item, CollectView v) {
                presenter.collect(item, v);
            }

            @Override
            public void uncollect(ArticleBean item, CollectView v) {
                presenter.uncollect(item, v);
            }
        });
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mAdapter.closeAll(null);
            ArticleBean item = mAdapter.getItem(position);
            if (item == null) {
                return;
            }
            switch (view.getId()) {
                default:
                    break;
                case R.id.tv_delete:
                    presenter.deleteMineShareArticle(item);
                    break;
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            currPage = PAGE_START;
            presenter.getMineShareArticleList(currPage, true);
        });
    }

    @Override
    protected void bindData() {
        MultiStateUtils.toLoading(msv);
        currPage = PAGE_START;
        presenter.getMineShareArticleList(currPage, false);
    }

    @Override
    public void getMineShareArticleListSuccess(int code, ArticleListBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == 1) {
            mAdapter.setNewData(data.getDatas());
            mAdapter.setEnableLoadMore(true);
            if (data.getDatas() == null || data.getDatas().isEmpty()) {
                MultiStateUtils.toEmpty(msv);
            } else {
                MultiStateUtils.toContent(msv);
            }
        } else {
            mAdapter.addData(data.getDatas());
            mAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mAdapter.loadMoreEnd();
        }
        mSmartRefreshUtils.success();
    }

    @Override
    public void getMineShareArticleListFailed(int code, String msg) {
        ToastMaker.showShort(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            MultiStateUtils.toError(msv);
        }
    }
}
