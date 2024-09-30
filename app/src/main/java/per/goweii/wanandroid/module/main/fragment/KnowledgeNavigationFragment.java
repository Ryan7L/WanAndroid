package per.goweii.wanandroid.module.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import per.goweii.actionbarex.ActionBarEx;
import per.goweii.basic.core.adapter.FixedFragmentPagerAdapter;
import per.goweii.basic.core.base.BaseFragment;
import per.goweii.basic.utils.listener.SimpleCallback;
import per.goweii.wanandroid.R;
import per.goweii.wanandroid.common.Config;
import per.goweii.wanandroid.databinding.FragmentKnowledgeNavigationBinding;
import per.goweii.wanandroid.module.knowledge.fragment.KnowledgeFragment;
import per.goweii.wanandroid.module.navigation.fragment.NaviFragment;
import per.goweii.wanandroid.utils.MagicIndicatorUtils;
import per.goweii.wanandroid.utils.RvScrollTopUtils;

/**
 * @author CuiZhen
 * @date 2019/5/19
 * GitHub: https://github.com/goweii
 */
public class KnowledgeNavigationFragment extends BaseFragment implements RvScrollTopUtils.ScrollTop {

    //    @BindView(R.id.ab)
    ActionBarEx ab;
    //    @BindView(R.id.vp)
    ViewPager vp;

    private FixedFragmentPagerAdapter mAdapter;
    private long lastClickTime = 0L;
    private int lastClickPos = 0;
    private FragmentKnowledgeNavigationBinding binding;

    public static KnowledgeNavigationFragment create() {
        return new KnowledgeNavigationFragment();
    }


    @Override
    protected View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentKnowledgeNavigationBinding.inflate(inflater, container, false);
        mRootView = binding.getRoot();
        ab = binding.ab;
        vp = binding.vp;
        return mRootView;
    }

    @Override
    protected void initViews() {
        mAdapter = new FixedFragmentPagerAdapter(getChildFragmentManager());
        mAdapter.setTitles("体系", "导航");
        mAdapter.setFragmentList(
                KnowledgeFragment.create(),
                NaviFragment.create()
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

    @Override
    public void onVisible(boolean isFirstVisible) {
        super.onVisible(isFirstVisible);
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            Fragment fragment = mAdapter.getItem(vp.getCurrentItem());
            if (fragment instanceof RvScrollTopUtils.ScrollTop) {
                RvScrollTopUtils.ScrollTop scrollTop = (RvScrollTopUtils.ScrollTop) fragment;
                scrollTop.scrollTop();
            }
        }
    }

    private void notifyScrollTop(int pos) {
        long currClickTime = System.currentTimeMillis();
        if (lastClickPos == pos && currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
            Fragment fragment = mAdapter.getItem(vp.getCurrentItem());
            if (fragment instanceof RvScrollTopUtils.ScrollTop) {
                RvScrollTopUtils.ScrollTop scrollTop = (RvScrollTopUtils.ScrollTop) fragment;
                scrollTop.scrollTop();
            }
        }
        lastClickPos = pos;
        lastClickTime = currClickTime;
    }
}
