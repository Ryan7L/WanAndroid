package per.goweii.basic.core.base

import org.greenrobot.eventbus.EventBus
import per.goweii.basic.core.CoreInit
import per.goweii.basic.core.mvp.MvpActivity
import per.goweii.basic.core.receiver.LoginReceiver
import per.goweii.basic.core.utils.LoadingBarManager
import per.goweii.basic.ui.dialog.LoadingDialog

abstract class BaseActivity<P : BasePresenter<V>, V : BaseView> : MvpActivity<P, V>() {
    private var loadingDialog: LoadingDialog? = null
    private var loadingBarManager: LoadingBarManager? = null
    private var loginReceiver: LoginReceiver? = null

    /**
     * 是否注册事件分发，默认不绑定
     */
    protected open val isRegisterEventBus = false
    override fun initialize() {
        if (isRegisterEventBus) {
            EventBus.getDefault().register(this)
        }
        super.initialize()
    }

    override fun onResume() {
        super.onResume()
        loginReceiver = LoginReceiver.register(this) {
            if (it == LoginReceiver.NOT_LOGIN) {
                CoreInit.getInstance().onGoLoginCallback?.onResult(getActivity())
            }
        }
    }

    override fun onPause() {
        super.onPause()
        loginReceiver?.unregister()
    }

    override fun onDestroy() {
        clearLoading()
        super.onDestroy()
        if (isRegisterEventBus) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.with(viewContext)
        }
        loadingDialog?.show()
    }

    override fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun dismissLoadingBar() {
        loadingBarManager?.dismiss()
    }

    override fun showLoadingBar() {
        if (loadingBarManager == null) {
            loadingBarManager = LoadingBarManager.attach(window.decorView)
        }
        loadingBarManager?.show()
    }

    override fun clearLoading() {
        loadingBarManager?.clear()
        loadingBarManager?.detach()
        loadingBarManager = null
        loadingDialog?.clear()
        loadingDialog = null
    }
}