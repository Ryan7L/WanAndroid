package per.goweii.wanandroid.common

import BaseApp
import per.goweii.basic.utils.InitTaskRunner

class WanApp : BaseApp() {
    override fun onCreate() {
        super.onCreate()
        InitTaskRunner(this)
            .add(SmartRefreshInitTask())
            .add(CookieManagerInitTask())
            .add(NightModeInitTask())
            .add(ThemeInitTask())
            .add(GrayFilterInitTask())
            .add(SwipeBackInitTask())
            .add(CoreInitTask())
            .add(RxHttpInitTask())
            .add(WanDbInitTask())
            .add(WanCacheInitTask())
            .add(BlurredInitTask())
            .add(X5InitTask())
            .add(CrashInitTask())
            .add(ReadingModeTask())
            .add(LoggerInitTask())
            .run()
    }
}