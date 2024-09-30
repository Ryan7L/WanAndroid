import android.content.Context
import android.content.res.Configuration
import io.reactivex.plugins.RxJavaPlugins
import per.goweii.basic.core.base.App
import per.goweii.basic.core.base.AppLike
import per.goweii.basic.utils.LogUtils
import per.goweii.basic.utils.Utils

abstract class BaseApp : App() {

    private val appLikeList = mutableListOf<AppLike>()

    companion object {
        private val APP_LIKE_LIST = mutableListOf<String>()
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        findAppLike()
        onAppLikeAttachBaseContext(context)
    }

    override fun onCreate() {
        super.onCreate()
        Utils.setUp(this)
        RxJavaPlugins.setErrorHandler { throwable -> throwable.printStackTrace() }
        onAppLikeCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        onAppLikeConfigurationChanged(newConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
        onAppLikeTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        onAppLikeLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        onAppLikeTrimMemory(level)
    }

    private fun onAppLikeAttachBaseContext(context: Context) {
        appLikeList.forEach { appLike ->
            val ts = System.currentTimeMillis()
            appLike.attachBaseContext(context)
            val te = System.currentTimeMillis()
            logAppLikeInfo(appLike, "attachBaseContext", te - ts)
        }
    }

    private fun onAppLikeCreate() {
        appLikeList.forEach { appLike ->
            val ts = System.currentTimeMillis()
            appLike.onCreate(getApp())
            val te = System.currentTimeMillis()
            logAppLikeInfo(appLike, "onCreate", te - ts)
        }
    }

    private fun onAppLikeConfigurationChanged(newConfig: Configuration) {
        appLikeList.forEach { appLike ->
            val ts = System.currentTimeMillis()
            appLike.onConfigurationChanged(getApp(), newConfig)
            val te = System.currentTimeMillis()
            logAppLikeInfo(appLike, "onConfigurationChanged", te - ts)
        }
    }

    private fun onAppLikeTerminate() {
        appLikeList.forEach { appLike ->
            val ts = System.currentTimeMillis()
            appLike.onTerminate(getApp())
            val te = System.currentTimeMillis()
            logAppLikeInfo(appLike, "onTerminate", te - ts)
        }
    }

    private fun onAppLikeLowMemory() {
        appLikeList.forEach { appLike ->
            val ts = System.currentTimeMillis()
            appLike.onLowMemory(getApp())
            val te = System.currentTimeMillis()
            logAppLikeInfo(appLike, "onLowMemory", te - ts)
        }
    }

    private fun onAppLikeTrimMemory(level: Int) {
        appLikeList.forEach { appLike ->
            val ts = System.currentTimeMillis()
            appLike.onTrimMemory(getApp(), level)
            val te = System.currentTimeMillis()
            logAppLikeInfo(appLike, "onTrimMemory", te - ts)
        }
    }

    private fun findAppLike() {
        APP_LIKE_LIST.forEach { classPath ->
            try {
                val clazz = Class.forName(classPath)
                val appLike = clazz.newInstance() as AppLike
                appLikeList.add(appLike)
            } catch (ignore: Exception) {
            }
        }
    }

    private fun logAppLikeInfo(a: AppLike, msg: String, d: Long) {
        LogUtils.i(a::class.java.simpleName, "$msg [$d ms]")
    }
}
