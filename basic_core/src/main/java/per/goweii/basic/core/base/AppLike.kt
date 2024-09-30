package per.goweii.basic.core.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration

interface AppLike {
    fun attachBaseContext(context: Context)

    fun onCreate(app: Application?)

    fun onConfigurationChanged(app: Application, newConfig: Configuration?)

    fun onTerminate(app: Application)

    fun onLowMemory(app: Application)

    fun onTrimMemory(app: Application, level: Int)
}