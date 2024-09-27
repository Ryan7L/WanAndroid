package per.goweii.basic.core.mvp

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import per.goweii.basic.utils.ClickHelper

abstract class IMvpActivity<P : IPresenter<IView>, VB : ViewBinding> : CacheActivity(), IView,
    View.OnClickListener {
    private lateinit var presenter: P
    private lateinit var binding: VB
    protected abstract fun initContentView()
    protected abstract fun initPresenter()
    protected abstract fun initViews()
    protected fun onClickContinuously(v: View): Boolean {
        return false
    }

    protected fun onClickSpace(v: View) {}
    protected fun initWindowConfig() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowConfig()
        initContentView()
        initPresenter()
        if (::presenter.isInitialized) {
            presenter.attach(this)
        }
        initViews()
        bindData()
    }

    protected abstract fun bindData()
    override fun onDestroy() {
        presenter.detach()
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        v?.let {
            if (!onClickContinuously(it)) {
                ClickHelper.onlyFirstSameView(it) { view ->
                    onClickSpace(view)
                }
            }
        }

    }
}