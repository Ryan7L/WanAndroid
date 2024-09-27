package per.goweii.basic.utils

import android.app.Application
import android.content.Context

class Utils {
    companion object {
        private var appContext: Application? = null

        @JvmStatic
        fun setUp(app: Application) {
            appContext = app
        }

        @JvmStatic
        val context: Context
            get() = appContext?.baseContext
                ?: throw NullPointerException("Context is null,please call setUp first!!!")
    }
}