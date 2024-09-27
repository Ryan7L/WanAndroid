package per.goweii.wanandroid.event

import per.goweii.wanandroid.module.mine.model.MessageBean

class MessageDeleteEvent private constructor(val messageBean: MessageBean) : BaseEvent() {
    companion object {
        @JvmStatic
        fun post(messageBean: MessageBean) = MessageDeleteEvent(messageBean).post()
    }
}