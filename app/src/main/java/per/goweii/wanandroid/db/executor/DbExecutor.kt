package per.goweii.wanandroid.db.executor

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author CuiZhen
 * @date 2020/3/21
 */
open class DbExecutor : CoroutineScope by MainScope() {
    //MainScope:它创建了一个绑定到主线程的 CoroutineScope。
    //这意味着在 MainScope 中启动的协程会在 Android 的主线程上运行，这对于更新 UI 界面非常有用。

    fun destroy() {
        cancel()
    }

    fun <T> execute(
        runnable: suspend () -> T,
        success: ((t: T) -> Unit)? = null,
        error: ((e: Throwable) -> Unit)? = null
    ) {
        launch(CoroutineExceptionHandler { _, _ -> }) {
            val result: T = withContext(Dispatchers.IO) {
                runnable.invoke()
            }
            success?.invoke(result)
        }.invokeOnCompletion {
            it?.let {
                error?.invoke(it)
            }
        }
    }
}