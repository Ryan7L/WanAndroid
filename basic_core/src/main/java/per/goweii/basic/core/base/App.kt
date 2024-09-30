package per.goweii.basic.core.base

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import androidx.multidex.MultiDex
import per.goweii.basic.utils.ProcessUtils

open class App : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        private var application: Application? = null
        private val activities = mutableListOf<Activity>()

        @JvmStatic
        fun getApp(): Application {
            return application
                ?: throw NullPointerException("App is not registered in the manifest")
        }

        @JvmStatic
        fun getAppContext(): Context {
            return getApp().applicationContext
        }
        @JvmStatic
        fun getActivities(): List<Activity> {
            return activities
        }

        @JvmStatic
        fun isAppAlive(): Boolean {
            return application != null && activities.isNotEmpty()
        }

        @JvmStatic
        fun isForeground(): Boolean {
            val activityManager =
                getAppContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appProcesses = activityManager.runningAppProcesses ?: return false
            val packageName = getAppContext().packageName
            return appProcesses.any { it.processName == packageName && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND }
        }

        @JvmStatic
        fun bringToForeground() {
            if (!isForeground()) {
                currentActivity()?.let { currentActivity ->
                    val intent = Intent(getAppContext(), currentActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    getAppContext().startActivity(intent)
                }
            }
        }

        @JvmStatic
        fun currentActivity(): Activity? {
            return activities.lastOrNull()
        }

        @JvmStatic
        fun findActivity(cls: Class<*>?): Activity? {
            return cls?.let {
                activities.firstOrNull { it.javaClass == cls }
            }
        }

        @JvmStatic
        fun finishCurrentActivity() {
            finishActivity(currentActivity())
        }

        @JvmStatic
        fun finishActivity(activity: Activity?) {
            activity?.let {
                activities.remove(it)
                it.finish()
            }
        }

        @JvmStatic
        fun finishActivity(cls: Class<out Activity>?) {
            cls?.let {
                activities.reversed().forEach { activity ->
                    if (cls == activity.javaClass) {
                        finishActivity(activity)
                    }
                }
            }
        }

        @JvmStatic
        fun finishAllActivity() {
            activities.reversed().forEach { activity ->
                if (!activity.isFinishing) activity.finish()
            }
            activities.clear()
        }

        @JvmStatic
        fun exitApp() {
            finishAllActivity()
            killProcess()
        }

        @JvmStatic
        fun killProcess() {
            Process.killProcess(Process.myPid())
        }

        @JvmStatic
        fun restartApp() {
            val intent =
                getApp().packageManager.getLaunchIntentForPackage(getApp().packageName)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                }
            if (intent != null) {
                getApp().startActivity(intent)
                killProcess()
            } else {
                finishActivityWithoutCount(1)
                activities.firstOrNull()?.recreate()
            }
        }

        @JvmStatic
        fun recreate() {
            activities.forEach { it.recreate() }
        }

        @JvmStatic
        fun finishActivityWithoutCount(count: Int) {
            if (count <= 0) {
                finishAllActivity()
                return
            }
            activities.takeLast(activities.size - count).forEach { finishActivity(it) }
        }

        @JvmStatic
        fun finishActivityWithout(cls: Class<out Activity>?) {
            if (cls == null) {
                finishAllActivity()
            } else {
                activities.reversed().forEach { activity ->
                    if (cls != activity.javaClass) {
                        finishActivity(activity)
                    }
                }
            }
        }

        @JvmStatic
        fun finishActivityWithout(activity: Activity?) {
            finishActivityWithout(activity?.javaClass)
        }

        @JvmStatic
        fun isMainProcess(): Boolean {
            val mainProcessName = getAppContext().packageName
            val processName = getCurrentProcessName()
            return processName == mainProcessName
        }

        @JvmStatic
        fun getCurrentProcessName(): String {
            return ProcessUtils.getCurrentProcessName(getAppContext())
        }
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        application = this
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activities.add(activity)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        activities.remove(activity)
    }
}
