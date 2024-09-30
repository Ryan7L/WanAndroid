package per.goweii.wanandroid.module.main.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import per.goweii.basic.core.base.App
import per.goweii.basic.core.base.BaseActivity
import per.goweii.basic.core.base.BasePresenter
import per.goweii.basic.core.base.BaseView
import per.goweii.basic.utils.LogUtils
import per.goweii.swipeback.SwipeBackAbility
import per.goweii.swipeback.SwipeBackDirection
import per.goweii.wanandroid.utils.UrlOpenUtils

private const val TAG = "RouterActivity"
class RouterActivity: BaseActivity<BasePresenter<BaseView>,BaseView>(),SwipeBackAbility.Direction,Runnable {
    private var handler: Handler? = null
    override fun swipeBackDirection(): SwipeBackDirection {
        return SwipeBackDirection.NONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = App.findActivity(MainActivity::class.java)
        LogUtils.i(TAG, "findActivity=$activity")
        activity?.let {
            parseIntent(intent)
            finish()
        } ?: {
            LogUtils.i(TAG, "start MainActivity")
            MainActivity.start(viewContext)
            afterMainActivityStarted()
        }
    }

    override fun onDestroy() {
        handler?.removeCallbacks(this)
        super.onDestroy()
    }
    private fun afterMainActivityStarted(){
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(this,100)
    }

    override fun run() {
        val activity = App.findActivity(MainActivity::class.java)
        LogUtils.i(TAG, "findActivity=$activity")
        activity?.let {
            parseIntent(intent)
            finish()
        } ?: handler?.postDelayed(this,100)
    }
    private fun parseIntent(intent: Intent){
        val action = intent.action
        LogUtils.d(TAG, "action=$action")
        if (action == null) {
            return
        }
        when(action){
            Intent.ACTION_VIEW -> handleOpenUrl(intent.data)
            Intent.ACTION_SEND -> handleShareText(intent.getStringExtra(Intent.EXTRA_TEXT))
            else -> {}
        }
    }
    private fun handleOpenUrl(data: Uri?){
        LogUtils.d(TAG, "data=$data")
        data?:return
        val scheme = data.scheme
        if (scheme != "http" && scheme != "https"){
            return
        }
        UrlOpenUtils.with(data.toString())
            .open(viewContext)
    }
    private fun handleShareText(text: String?) {
        try {
            LogUtils.d(TAG, "sharedText=$text")
            text ?: return
            if (text.isEmpty()) {
                return
            }
            var urlStartIndex = text.indexOf("https://")
            if (urlStartIndex < 0) {
                urlStartIndex = text.indexOf("http://")
            }
            if (urlStartIndex < 0) {
                return
            }
            val msg = if (urlStartIndex > 0) {
                text.substring(0, urlStartIndex - 1)
            } else {
                ""
            }
            LogUtils.d(TAG, "sharedMsg=$msg")
            val url = text.substring(urlStartIndex).takeWhile { it != ' ' }
            LogUtils.d(TAG, "sharedUrl=$url")
            viewContext?.let { ShareArticleActivity.start(it, msg, url) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}