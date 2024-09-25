package per.goweii.wanandroid.module.navigation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kennyc.view.MultiStateView;

import java.util.List;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.listener.SimpleListener;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.FragmentKnowledgeNavigationChildBinding;
import per.goweii.wanandroid.module.main.model.ArticleBean;
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
public class NaviFragment extends BaseFragment<NaviPresenter> implements RvScrollTopUtils.ScrollTop, NaviView {

    //@BindView(R.id.msv)
    MultiStateView msv;
    //@BindView(R.id.rv)
    RecyclerView rv;

    private NaviAdapter mAdapter;

    public static NaviFragment create() {
        return new NaviFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentKnowledgeNavigationChildBinding binding = FragmentKnowledgeNavigationChildBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        mViewCreated = true;
        msv = binding.msv;
        rv = binding.rv;
        return mRootView;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_knowledge_navigation_child;
    }

    @Nullable
    @Override
    protected NaviPresenter initPresenter() {
        return new NaviPresenter();
    }

    @Override
    protected void initView() {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NaviAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnItemClickListener(new NaviAdapter.OnItemClickListener() {
            @Override
            public void onClick(ArticleBean bean, int pos) {
                UrlOpenUtils.Companion.with(bean).open(getContext());
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new SimpleListener() {
            @Override
            public void onResult() {
                MultiStateUtils.toLoading(msv);
                presenter.getKnowledgeList();
            }
        });
    }

    @Override
    protected void loadData() {
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
