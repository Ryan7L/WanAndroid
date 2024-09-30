package per.goweii.wanandroid.event

class MessageCountEvent private constructor(val count: Int) : BaseEvent() {
    companion object {
        @JvmStatic
        fun post(count: Int) {
            MessageCountEvent(count).post()
        }
    }
}