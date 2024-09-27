package per.goweii.wanandroid.event

class SettingChangeEvent : BaseEvent() {
    var isShowTopChanged = false
    var isShowBannerChanged = false
    override fun post() {
        if (isShowBannerChanged || isShowTopChanged) {
            super.post()
        }
    }
}