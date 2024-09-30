package per.goweii.wanandroid.module.main.activity

import android.content.Context
import android.content.Intent
import per.goweii.anypermission.AnyPermission
import per.goweii.anypermission.RequestListener
import per.goweii.basic.core.base.App
import per.goweii.basic.core.base.BaseActivity
import per.goweii.basic.core.base.BasePresenter
import per.goweii.basic.core.base.BaseView
import per.goweii.basic.utils.LogUtils
import per.goweii.wanandroid.utils.ThemeUtils
import java.io.File

class InstallApkActivity : BaseActivity<BasePresenter<BaseView>, BaseView>() {

    companion object {
        private const val APK_PATH = "apk_path"

        @JvmStatic
        fun start(context: Context, apk: File) {
            val intent = Intent(context, InstallApkActivity::class.java)
            intent.putExtra(APK_PATH, apk.path)
            context.startActivity(intent)
        }
    }

    private var apkFile: File? = null

    override fun initViews() {
        val path = intent.getStringExtra(APK_PATH)
        if (path.isNullOrEmpty()) {
            finish()
            return
        }
        apkFile = File(path)
        apkFile?.let {
            if (!it.exists()) {
                finish()
            }
        }
    }

    override fun bindData() {
    }

    override fun onResume() {
        super.onResume()
        apkFile?.let {
            if (!it.exists()) {
                finish()
                return
            }
            AnyPermission.with(this)
                .install(apkFile)
                .request(object : RequestListener {
                    override fun onSuccess() {
                        LogUtils.d("ThemeUtils", "installApk")
                        if (!ThemeUtils.isDefLauncher(applicationContext)) {
                            App.finishAllActivity()
                            ThemeUtils.resetLauncher(applicationContext)
                            start(this@InstallApkActivity, it)
                        }
                    }

                    override fun onFailed() {
                        ThemeUtils.setNotInstall()
                        finish()
                    }
                })
        }
    }
}