package per.goweii.wanandroid.module.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.core.utils.SmartRefreshUtils;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.FragmentCollectionArticleBinding;
import per.goweii.wanandroid.event.CollectionEvent;
import per.goweii.wanandroid.module.main.adapter.ArticleAdapter;
import per.goweii.wanandroid.module.main.model.ArticleBean;
import per.goweii.wanandroid.module.main.model.ArticleListBean;
import per.goweii.wanandroid.module.mine.presenter.CollectionArticlePresenter;
import per.goweii.wanandroid.module.mine.view.CollectionArticleView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;
import per.goweii.wanandroid.utils.UrlOpenUtils;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class CollectionArticleFragment extends BaseFragment<CollectionArticlePresenter, CollectionArticleView> implements RvScrollTopUtils.ScrollTop, CollectionArticleView {

    public static final int PAGE_START = 0;

    MultiStateView msv;
    SmartRefreshLayout srl;
    RecyclerView rv;
    private SmartRefreshUtils mSmartRefreshUtils;
    private ArticleAdapter mAdapter;
    private int currPage = PAGE_START;

    public static CollectionArticleFragment create() {
        return new CollectionArticleFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCollectionArticleBinding binding = FragmentCollectionArticleBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        msv = binding.msv;
        srl = binding.srl;
        rv = binding.rv;
        return mRootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionEvent(CollectionEvent event) {
        if (isDetached()) {
            return;
        }
        if (event.isCollect()) {
            currPage = PAGE_START;
            presenter.getCollectArticleList(currPage, true);
        } else {
            presenter.updateCollectArticleList(PAGE_START);
            if (event.getArticleId() != -1 || event.getCollectId() != -1) {
                mAdapter.forEach((dataPos, adapterPos, bean) -> {
                    if (event.getArticleId() != -1) {
                        if (bean.getOriginId() == event.getArticleId()) {
                            mAdapter.remove(adapterPos);
                            return true;
                        }
                    } else if (event.getCollectId() != -1) {
                        if (bean.getId() == event.getCollectId()) {
                            mAdapter.remove(adapterPos);
                            return true;
                        }
                    }
                    return false;
                });
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_collection_article;
    }

    @Override
    protected void setUpPresenter() {
        presenter = new CollectionArticlePresenter();
    }

    @Override
    protected void initViews() {
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(() -> {
            currPage = PAGE_START;
            presenter.getCollectArticleList(currPage, true);
        });
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new ArticleAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(() -> presenter.getCollectArticleList(currPage, true), rv);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleBean item = mAdapter.getItem(position);
            if (item != null) {
                UrlOpenUtils.Companion.with(item).open(getViewContext());
            }
        });
        mAdapter.setOnItemChildViewClickListener((helper, v, position) -> {
            ArticleBean item = mAdapter.getItem(position);
            if (item != null) {
                presenter.uncollectArticle(item, v);
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            currPage = PAGE_START;
            presenter.getCollectArticleList(currPage, true);
        });
    }

    @Override
    protected void bindData() {
        MultiStateUtils.toLoading(msv);
        presenter.getCollectArticleListCache(currPage);
    }

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
        if (isFirstVisible) {
            currPage = PAGE_START;
            presenter.getCollectArticleList(currPage, true);
        }
    }

    @Override
    public void getCollectArticleListSuccess(int code, ArticleListBean data) {
        if (data.getDatas() != null) {
            for (ArticleBean articleBean : data.getDatas()) {
                articleBean.setCollect(true);
            }
        }
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
    public void getCollectArticleListFailed(int code, String msg) {
        ToastMaker.showShort(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            MultiStateUtils.toError(msv);
        }
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
}
