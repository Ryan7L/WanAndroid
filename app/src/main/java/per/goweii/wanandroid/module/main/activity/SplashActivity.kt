package per.goweii.wanandroid.module.main.activity

import per.goweii.basic.core.base.BaseActivity
import per.goweii.basic.core.base.BasePresenter
import per.goweii.basic.core.base.BaseView
import per.goweii.wanandroid.R

class SplashActivity: BaseActivity<BasePresenter<BaseView>,BaseView>() {
    override fun initViews() {
        MainActivity.start(viewContext)
        finish()
        overridePendingTransition(R.anim.zoom_small_in, R.anim.zoom_small_out)
    }

    override fun bindData() {
    }
}