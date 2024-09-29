package per.goweii.basic.core.mvp

import android.content.Context

interface IView {
    val context: Context?

    fun showLoadingDialog()

    fun dismissLoadingDialog()

    fun showLoadingBar()

    fun dismissLoadingBar()

    fun clearLoading()
}