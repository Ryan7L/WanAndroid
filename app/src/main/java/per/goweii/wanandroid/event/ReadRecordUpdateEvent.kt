package per.goweii.wanandroid.event

class ReadRecordUpdateEvent(val link: String) : BaseEvent() {
    var title: String? = null
    var time: Long = 0
    var percent: Float = 0f
}