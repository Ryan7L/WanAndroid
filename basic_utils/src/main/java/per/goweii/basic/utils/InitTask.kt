package per.goweii.basic.utils

import android.app.Application
import android.os.Process
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class InitTaskRunner(private val application: Application) {

    private val tasks: MutableList<InitTask> = mutableListOf()

    fun add(task: InitTask): InitTaskRunner {
        tasks.add(task)
        return this
    }

    fun run() {
        val isMainProcess = isMainProcess()
        val syncTasks = tasks.filter { !(!isMainProcess && it.onlyMainProcess()) && it.sync() }
        val asyncTasks = tasks.filter { !(!isMainProcess && it.onlyMainProcess()) && !it.sync() }

        runSync(syncTasks)
        runAsync(asyncTasks)
    }

    private fun runSync(tasks: List<InitTask>) {
        tasks.sortedBy { it.level() }.forEach {
            try {
                it.init(application)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun runAsync(tasks: List<InitTask>) {
        val tasksMap = tasks.groupBy { it.asyncTaskName() }
        tasksMap.values.forEach { taskList ->
            CoroutineScope(Dispatchers.IO).launch {
                taskList.sortedBy { it.level() }.forEach {
                    try {
                        it.init(application)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun isMainProcess(): Boolean {
        return application.packageName == getCurrentProcessName()
    }

    private fun getCurrentProcessName(): String? {
        return try {
            val file = File("/proc/" + Process.myPid() + "/" + "cmdline")
            BufferedReader(FileReader(file)).use { reader ->
                reader.readLine().trim { it <= ' ' }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

abstract class AsyncInitTask : InitTask {
    override fun sync(): Boolean = false
    override fun level(): Int = 0
    override fun onlyMainProcess(): Boolean = true
    override fun asyncTaskName(): String = this.toString()
}

abstract class SyncInitTask : InitTask {
    override fun sync(): Boolean = true
    override fun level(): Int = 0
    override fun onlyMainProcess(): Boolean = true
    override fun asyncTaskName(): String = ""
}

interface InitTask {
    fun sync(): Boolean
    fun asyncTaskName(): String
    fun level(): Int
    fun onlyMainProcess(): Boolean
    fun init(application: Application)
}