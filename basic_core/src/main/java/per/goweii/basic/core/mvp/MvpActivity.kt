package per.goweii.basic.core.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import per.goweii.basic.utils.ClickHelper

abstract class MvpActivity<P : IPresenter<V>, V : IView> : CacheActivity(), IView,
    View.OnClickListener {
    @JvmField
    var presenter: P? = null
    protected open fun setUpPresenter() {

    }

    protected open fun initViews(){}
    protected open fun bindData(){}

    /**
     * 点击事件，可连续点击
     */
    protected open fun onClickContinuously(v: View?): Boolean {
        return false
    }

    /**
     * 点击事件，500毫秒第一次
     */
    protected open fun onClickSpace(v: View?) {
    }

    protected open fun initWindowConfig() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindowConfig()
        initContentView()
        setUpPresenter()
        presenter?.attach(this as V)
        initialize()
    }

    protected open fun initContentView() {}
    protected open fun initialize() {
        initViews()
        bindData()
    }

    override fun onDestroy() {
        presenter?.detach()
        super.onDestroy()
    }

    override val viewContext: Context?
        get() = getActivity()

    protected open fun getActivity(): Activity {
        return this
    }

    override fun onClick(v: View?) {
        v?.let {
            if (!onClickContinuously(v)) {
                ClickHelper.onlyFirstSameView(v, this::onClickSpace)
            }
        }
    }
}