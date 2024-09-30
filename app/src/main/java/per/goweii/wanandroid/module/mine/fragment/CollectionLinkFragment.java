package per.goweii.wanandroid.module.mine.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.kennyc.view.MultiStateView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.core.utils.SmartRefreshUtils;
import per.goweii.basic.ui.toast.ToastMaker;
import per.goweii.basic.utils.CopyUtils;
import per.goweii.basic.utils.IntentUtils;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.databinding.FragmentCollectionLinkBinding;
import per.goweii.wanandroid.event.CollectionEvent;
import per.goweii.wanandroid.module.main.model.CollectionLinkBean;
import per.goweii.wanandroid.module.mine.adapter.CollectionLinkAdapter;
import per.goweii.wanandroid.module.mine.dialog.EditCollectLinkDialog;
import per.goweii.wanandroid.module.mine.presenter.CollectionLinkPresenter;
import per.goweii.wanandroid.module.mine.view.CollectionLinkView;
import per.goweii.wanandroid.utils.MultiStateUtils;
import per.goweii.wanandroid.utils.RvConfigUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;
import per.goweii.wanandroid.utils.UrlOpenUtils;

/**
 * @author CuiZhen
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class CollectionLinkFragment extends BaseFragment<CollectionLinkPresenter,CollectionLinkView> implements RvScrollTopUtils.ScrollTop, CollectionLinkView {

    MultiStateView msv;
    SmartRefreshLayout srl;
    RecyclerView rv;
    private SmartRefreshUtils mSmartRefreshUtils;
    private CollectionLinkAdapter mAdapter;

    public static CollectionLinkFragment create() {
        return new CollectionLinkFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCollectionLinkBinding binding = FragmentCollectionLinkBinding.inflate(inflater, container, false);
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
            presenter.getCollectLinkList(true);
        } else {
            presenter.updateCollectLinkList();
            if (event.getCollectId() != -1) {
                List<CollectionLinkBean> list = mAdapter.getData();
                for (int i = 0; i < list.size(); i++) {
                    CollectionLinkBean item = list.get(i);
                    if (item.getId() == event.getCollectId()) {
                        mAdapter.remove(i);
                        break;
                    }
                }
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void setUpPresenter() {
        presenter =  new CollectionLinkPresenter();
    }

    @Override
    protected void initViews() {
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(() -> presenter.getCollectLinkList(true));
        rv.setLayoutManager(new LinearLayoutManager(getViewContext()));
        mAdapter = new CollectionLinkAdapter();
        RvConfigUtils.init(mAdapter);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            mAdapter.closeAll(null);
            CollectionLinkBean item = mAdapter.getItem(position);
            if (item == null) {
                return;
            }
            switch (view.getId()) {
                default:
                    break;
                case R.id.rl_top:
                    UrlOpenUtils.Companion
                            .with(item.getLink())
                            .collectId(item.getId())
                            .title(item.getName())
                            .collected(true)
                            .open(getViewContext());
                    break;
                case R.id.tv_copy:
                    CopyUtils.copyText(item.getLink());
                    ToastMaker.showShort("复制成功");
                    break;
                case R.id.tv_open:
                    if (TextUtils.isEmpty(item.getLink())) {
                        ToastMaker.showShort("链接为空");
                        break;
                    }
                    if (getViewContext() != null) {
                        IntentUtils.openBrowser(getViewContext(), item.getLink());
                    }
                    break;
                case R.id.tv_edit:
                    EditCollectLinkDialog.show(getViewContext(), item, data -> presenter.updateCollectLink(data));
                    break;
                case R.id.tv_delete:
                    presenter.uncollectLink(item);
                    break;
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, () -> {
            MultiStateUtils.toLoading(msv);
            presenter.getCollectLinkList(true);
        });
        if (getRootView() != null) {
            ViewParent parent = getRootView().getParent();
            if (parent instanceof ViewPager) {
                ViewPager viewPager = (ViewPager) parent;
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float v, int i1) {
                    }

                    @Override
                    public void onPageSelected(int i) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {
                        if (i != ViewPager.SCROLL_STATE_IDLE) {
                            if (mAdapter != null) {
                                mAdapter.closeAll(null);
                            }
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void bindData() {
        MultiStateUtils.toLoading(msv);
        presenter.getCollectLinkListCache();
    }

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
        if (isFirstVisible) {
            presenter.getCollectLinkList(true);
        }
    }

    @Override
    public void getCollectLinkListSuccess(int code, List<CollectionLinkBean> data) {
        List<CollectionLinkBean> copyList;
        if (data != null) {
            copyList = new ArrayList<>(data);
        } else {
            copyList = new ArrayList<>(0);
        }
        Collections.reverse(copyList);
        mAdapter.setNewData(copyList);
        mSmartRefreshUtils.success();
        if (copyList.isEmpty()) {
            MultiStateUtils.toEmpty(msv);
        } else {
            MultiStateUtils.toContent(msv);
        }
    }

    @Override
    public void getCollectLinkListFailed(int code, String msg) {
        ToastMaker.showShort(msg);
        mSmartRefreshUtils.fail();
        MultiStateUtils.toError(msv);
    }

    @Override
    public void updateCollectLinkSuccess(int code, CollectionLinkBean data) {
        List<CollectionLinkBean> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            CollectionLinkBean bean = list.get(i);
            if (bean.getId() == data.getId()) {
                bean.setName(data.getName());
                bean.setLink(data.getLink());
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
}
