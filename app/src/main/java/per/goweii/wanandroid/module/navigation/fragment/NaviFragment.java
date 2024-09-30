package per.goweii.wanandroid.module.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennyc.view.MultiStateView;

import java.util.List;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.wanandroid.databinding.FragmentKnowledgeNavigationChildBinding;
import per.goweii.wanandroid.module.navigation.adapter.NaviAdapter;
import per.goweii.wanandroid.module.navigation.model.NaviBean;
import per.goweii.wanandroid.module.navigation.presenter.NaviPresenter;
import per.goweii.wanandroid.module.navigation.view.NaviView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;
import per.goweii.wanandroid.utils.UrlOpenUtils;

/**
 * @author CuiZhen
 * @date 2019/5/12
 * GitHub: https://github.com/goweii
 */
public class NaviFragment extends BaseFragment<NaviPresenter, NaviView> implements RvScrollTopUtils.ScrollTop, NaviView {

    MultiStateView msv;
    RecyclerView rv;

    private NaviAdapter mAdapter;

    public static NaviFragment create() {
        return new NaviFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentKnowledgeNavigationChildBinding binding = FragmentKnowledgeNavigationChildBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        msv = binding.msv;
        rv = binding.rv;
        return mRootView;
    }

    @Override
    protected void setUpPresenter() {
        presenter = new NaviPresenter();
    }

    @Override
    protected void initViews() {
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new NaviAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnItemClickListener((bean, pos) -> UrlOpenUtils.Companion.with(bean).open(getViewContext()));
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            presenter.getKnowledgeList();
        });
    }

    @Override
    protected void bindData() {
        MultiStateUtils.toLoading(msv);
        presenter.getKnowledgeListCache();
    }

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
        if (isFirstVisible) {
            presenter.getKnowledgeList();
        }
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }

    @Override
    public void getNaviListSuccess(int code, List<NaviBean> data) {
        mAdapter.setNewData(data);
        if (data == null || data.isEmpty()) {
            MultiStateUtils.toEmpty(msv);
        } else {
            MultiStateUtils.toContent(msv);
        }
    }

    @Override
    public void getNaviListFail(int code, String msg) {
        ToastMaker.showShort(msg);
        MultiStateUtils.toError(msv);
    }
}
