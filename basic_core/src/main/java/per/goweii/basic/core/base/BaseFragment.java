package per.goweii.basic.core.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

//import butterknife.ButterKnife;
//import butterknife.Unbinder;
import per.goweii.basic.core.mvp.MvpFragment;
import per.goweii.basic.core.utils.LoadingBarManager;
import per.goweii.basic.ui.dialog.LoadingDialog;

/**
 * @author Cuizhen
 * @version v1.0.0
 * @date 2018/3/10-下午12:38
 */
public abstract class BaseFragment<P extends BasePresenter<V>,V extends BaseView> extends MvpFragment<P,V> {
    private LoadingDialog mLoadingDialog = null;
    private LoadingBarManager mLoadingBarManager = null;

    /**
     * 是否注册事件分发，默认不绑定
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return setUpView(inflater, container, savedInstanceState);
    }

    private View setUpView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mRootView == null) {
            final int layoutId = getLayoutRes();
            if (layoutId > 0) {
                mRootView = initRootView(inflater, container, savedInstanceState);
            }
        }
        mViewCreated = true;
        return mRootView;
    }

    public View initRootView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    @Override
    protected void initialize() {
        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
        super.initialize();
    }

    @Override
    public void onDestroyView() {
        clearLoading();
        super.onDestroyView();
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialog.with(getContext());
        }
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showLoadingBar() {
        if (mLoadingBarManager == null) {
            mLoadingBarManager = LoadingBarManager.attach(getRootView());
        }
        mLoadingBarManager.show();
    }

    @Override
    public void dismissLoadingBar() {
        if (mLoadingBarManager != null) {
            mLoadingBarManager.dismiss();
        }
    }

    @Override
    public void clearLoading() {
        if (mLoadingBarManager != null) {
            mLoadingBarManager.clear();
            mLoadingBarManager.detach();
        }
        mLoadingBarManager = null;
        if (mLoadingDialog != null) {
            mLoadingDialog.clear();
        }
        mLoadingDialog = null;
    }

    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
