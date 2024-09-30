package per.goweii.wanandroid.module.knowledge.fragment;

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
import per.goweii.wanandroid.module.knowledge.activity.KnowledgeArticleActivity;
import per.goweii.wanandroid.module.knowledge.adapter.KnowledgeAdapter;
import per.goweii.wanandroid.module.knowledge.presenter.KnowledgePresenter;
import per.goweii.wanandroid.module.knowledge.view.KnowledgeView;
import per.goweii.wanandroid.module.main.model.ChapterBean;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;

/**
 * @author CuiZhen
 * @date 2019/5/12
 * GitHub: https://github.com/goweii
 */
public class KnowledgeFragment extends BaseFragment<KnowledgePresenter,KnowledgeView> implements RvScrollTopUtils.ScrollTop, KnowledgeView {

    //@BindView(R.id.msv)
    MultiStateView msv;
    //@BindView(R.id.rv)
    RecyclerView rv;

    private KnowledgeAdapter mAdapter;

    public static KnowledgeFragment create() {
        return new KnowledgeFragment();
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
        presenter =  new KnowledgePresenter();
    }

    @Override
    protected void initViews() {
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new KnowledgeAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {
            @Override
            public void onClick(ChapterBean bean, int pos) {
                KnowledgeArticleActivity.start(getViewContext(), bean, pos);
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
    public void getKnowledgeListSuccess(int code, List<ChapterBean> data) {
        mAdapter.setNewData(data);
        if (data == null || data.isEmpty()) {
            MultiStateUtils.toEmpty(msv);
        } else {
            MultiStateUtils.toContent(msv);
        }
    }

    @Override
    public void getKnowledgeListFail(int code, String msg) {
        ToastMaker.showShort(msg);
        MultiStateUtils.toError(msv);
    }
}
