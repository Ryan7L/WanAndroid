package per.goweii.wanandroid.event

import org.greenrobot.eventbus.EventBus

abstract class BaseEvent {
    open fun post() {
        EventBus.getDefault().post(this)
    }

    open fun postSticky() {
        EventBus.getDefault().postSticky(this)
    }

    open fun removeSticky() {
        EventBus.getDefault().removeStickyEvent(this)
    }

}