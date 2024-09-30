package per.goweii.basic.core.mvp

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import per.goweii.basic.utils.ClickHelper
import per.goweii.lazyfragment.LazyFragment

abstract class MvpFragment<P : IPresenter<V>, V : IView> : LazyFragment(), IView,
    View.OnClickListener {
    @JvmField
    var presenter: P? = null
    override fun getLayoutRes(): Int {
        return 0
    }

    protected open fun setUpPresenter() {

    }

    protected abstract fun initViews()
    protected abstract fun bindData()
    protected open fun onClickContinuously(v: View): Boolean {
        return false
    }

    protected open fun onClickSpace(v: View) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachPresenter()
        initialize()
    }

    override fun onVisible(isFirstVisible: Boolean) {
        super.onVisible(isFirstVisible)
        attachPresenter()
    }

    private fun attachPresenter() {
        if (presenter == null) {
            setUpPresenter()
        }
        presenter?.attach(this as V)
    }

    override fun onDestroyView() {
        presenter?.detach()
        super.onDestroyView()
    }

    protected open fun initialize() {
        initViews()
        bindData()
    }

    override val viewContext: Context?
        get() = activity

    val fragment: Fragment
        get() = this

    override fun onClick(v: View?) {
        v?.let {
            if (!onClickContinuously(it)) {
                ClickHelper.onlyFirstSameView(it, this::onClickSpace)
            }

        }
    }
}