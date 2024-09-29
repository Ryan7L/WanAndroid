package per.goweii.basic.core.base

import io.reactivex.disposables.Disposable
import per.goweii.basic.core.mvp.IPresenter
import per.goweii.rxhttp.core.RxLife

open class BasePresenter<V : BaseView> : IPresenter<V>() {
    private var rxLife: RxLife? = null
    override fun attach(baseView: V) {
        super.attach(baseView)
        rxLife = RxLife.create()
    }

    override fun detach() {
        super.detach()
        rxLife?.destroy()
        rxLife = null
    }

    fun getRxLife(): RxLife? {
        return rxLife
    }

    fun addToRxLife(disposable: Disposable) {
        rxLife?.add(disposable)

    }
}