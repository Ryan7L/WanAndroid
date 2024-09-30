package per.goweii.basic.core.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration

open class BaseAppLike: AppLike {
    override fun attachBaseContext(context: Context) {
        
    }

    override fun onCreate(app: Application?) {
        
    }

    override fun onConfigurationChanged(app: Application, newConfig: Configuration?) {
        
    }

    override fun onTerminate(app: Application) {
        
    }

    override fun onLowMemory(app: Application) {
        
    }

    override fun onTrimMemory(app: Application, level: Int) {
        
    }
}