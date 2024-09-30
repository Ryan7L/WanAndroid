package per.goweii.wanandroid.event

class HomeActionBarEvent(
    val homeTitle: String?,
    val actionBarBgColor: String?,
    val actionBarBgImageUrl: String?,
    val secondFloorBgImageUrl: String?,
    val secondFloorBgImageBlurPercent: Float
) : BaseEvent() {
    constructor() : this(null, null, null, null, 0f)
}