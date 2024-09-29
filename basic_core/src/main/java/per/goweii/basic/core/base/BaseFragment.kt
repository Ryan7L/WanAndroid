//package per.goweii.basic.core.base
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import org.greenrobot.eventbus.EventBus
//import per.goweii.basic.core.mvp.MvpFragment
//import per.goweii.basic.core.utils.LoadingBarManager
//import per.goweii.basic.ui.dialog.LoadingDialog
//
//abstract class BaseFragment<P : BasePresenter<V>, V : BaseView> : MvpFragment<P, V>() {
//    private var loadingDialog: LoadingDialog? = null
//    private var loadingBarManager: LoadingBarManager? = null
//
//    /**
//     * 是否注册事件分发，默认不绑定
//     */
//    protected open val isRegisterEventBus = false
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        if (mRootView == null) {
//            mRootView = initRootView(inflater, container, savedInstanceState)
//        }
//        mViewCreated = true
//        Log.d("TAG", "onCreateView:${mRootView == null} ")
//        return mRootView
//    }
//
//    protected open fun initRootView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        return inflater.inflate(0, container, false)
//    }
//
//    override fun initialize() {
//        if (isRegisterEventBus) {
//            EventBus.getDefault().register(this)
//        }
//        super.initialize()
//    }
//
//    override fun onDestroyView() {
//        clearLoading()
//        super.onDestroyView()
//        if (isRegisterEventBus) {
//            EventBus.getDefault().unregister(this)
//        }
//    }
//
//    override fun showLoadingDialog() {
//        if (loadingDialog == null) {
//            loadingDialog = LoadingDialog.with(viewContext)
//        }
//        loadingDialog?.show()
//    }
//
//    override fun dismissLoadingDialog() {
//        loadingDialog?.dismiss()
//    }
//
//    override fun showLoadingBar() {
//        if (loadingBarManager == null) {
//            loadingBarManager = LoadingBarManager.attach(rootView)
//        }
//        loadingBarManager?.show()
//    }
//
//    override fun dismissLoadingBar() {
//        loadingBarManager?.dismiss()
//    }
//
//    override fun clearLoading() {
//        loadingBarManager?.clear()
//        loadingBarManager?.dismiss()
//        loadingBarManager = null
//        loadingDialog?.clear()
//        loadingDialog = null
//    }
//
//    fun finish() {
//        activity?.finish()
//    }
//}