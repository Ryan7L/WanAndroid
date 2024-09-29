package per.goweii.basic.core.mvp

import android.content.Context

abstract class IPresenter<V : IView> {
    protected var context: Context? = null
    private var _baseView: V? = null

    open fun attach(baseView: V) {
        this._baseView = baseView
        context = baseView.context
    }

    open fun detach() {
        _baseView = null
        context = null
    }

    val baseView: V
        get() = _baseView ?: throw NullPointerException("baseView is null")
    val isAttach: Boolean
        get() = _baseView != null
    fun showLoadingDialog() {
        _baseView?.showLoadingDialog()
    }

    fun dismissLoadingDialog() {
        _baseView?.dismissLoadingDialog()
    }

    fun showLoadingBar() {
        _baseView?.showLoadingBar()
    }

    fun dismissLoadingBar() {
        _baseView?.dismissLoadingBar()
    }
}