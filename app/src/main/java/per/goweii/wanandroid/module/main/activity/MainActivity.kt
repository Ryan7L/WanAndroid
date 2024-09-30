package per.goweii.wanandroid.module.main.activity

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import per.goweii.anylayer.Layer
import per.goweii.anypermission.RuntimeRequester
import per.goweii.basic.core.adapter.FixedFragmentPagerAdapter
import per.goweii.basic.core.base.BaseActivity
import per.goweii.basic.utils.LogUtils
import per.goweii.basic.utils.ResUtils
import per.goweii.wanandroid.R
import per.goweii.wanandroid.databinding.ActivityMainBinding
import per.goweii.wanandroid.db.model.ReadLaterModel
import per.goweii.wanandroid.event.BannerAutoSwitchEnableEvent
import per.goweii.wanandroid.event.CloseSecondFloorEvent
import per.goweii.wanandroid.module.main.dialog.CopiedLinkDialog
import per.goweii.wanandroid.module.main.dialog.PasswordDialog
import per.goweii.wanandroid.module.main.dialog.PrivacyPolicyDialog
import per.goweii.wanandroid.module.main.fragment.MainFragment
import per.goweii.wanandroid.module.main.fragment.UserArticleFragment
import per.goweii.wanandroid.module.main.model.AdvertBean
import per.goweii.wanandroid.module.main.model.ConfigBean
import per.goweii.wanandroid.module.main.model.UpdateBean
import per.goweii.wanandroid.module.main.presenter.MainPresenter
import per.goweii.wanandroid.module.main.view.MainView
import per.goweii.wanandroid.utils.ConfigUtils
import per.goweii.wanandroid.utils.CopiedTextProcessor
import per.goweii.wanandroid.utils.PredefinedTaskQueen
import per.goweii.wanandroid.utils.RecommendManager
import per.goweii.wanandroid.utils.ThemeUtils
import per.goweii.wanandroid.utils.UpdateUtils
import per.goweii.wanandroid.utils.wanpwd.WanPwdParser


const val REQ_CODE_PERMISSION = 1


private const val taskPrivacyPolicy = "PrivacyPolicy"
private const val taskUpdate = "Update"
private const val taskBetaUpdate = "BetaUpdate"
private const val taskDownload = "Download"
private const val taskAdvert = "Advert"
private const val taskWanPwd = "WanPwd"
private const val taskCopiedLink = "CopiedLink"
private const val taskReadLater = "ReadLater"

class MainActivity : BaseActivity<MainPresenter, MainView>(), MainView {
    private val predefinedTaskQueen = PredefinedTaskQueen()
    private var adapter: FixedFragmentPagerAdapter? = null
    private var updateUtils: UpdateUtils? = null
    private var copiedLinkDialog: CopiedLinkDialog? = null
    private var passwordDialog: PasswordDialog? = null
    private lateinit var binding: ActivityMainBinding
    private var runtimeRequester: RuntimeRequester? = null

    companion object {
        @JvmStatic
        fun start(context: Context) {
            Intent(context, MainActivity::class.java).runCatching {
                context.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            LogUtils.d("ThemeUtils", "MainActivity onCreate setNotInstall")
            ThemeUtils.setNotInstall()
        }
    }

    override fun initContentView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initWindowConfig() {
        super.initWindowConfig()
        window.setBackgroundDrawable(
            ColorDrawable(
                ResUtils.getThemeColor(
                    this,
                    R.attr.colorBackground
                )
            )
        )
    }

    override fun setUpPresenter() {
        presenter = MainPresenter()
    }

    override fun initViews() {
        binding.vp.offscreenPageLimit = 1
        adapter = FixedFragmentPagerAdapter(supportFragmentManager)
        binding.vp.adapter = adapter
        adapter?.setFragmentList(UserArticleFragment.create(), MainFragment.create())
        binding.vp.currentItem = 1
        binding.vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                CloseSecondFloorEvent().post()
            }
        })
        predefinedTaskQueen.run {
            append(PredefinedTaskQueen.Task(taskPrivacyPolicy))
            append(PredefinedTaskQueen.Task(taskUpdate))
            append(PredefinedTaskQueen.Task(taskBetaUpdate))
            append(PredefinedTaskQueen.Task(taskDownload))
            append(PredefinedTaskQueen.Task(taskAdvert))
            append(PredefinedTaskQueen.Task(taskWanPwd))
            append(PredefinedTaskQueen.Task(taskCopiedLink))
            append(PredefinedTaskQueen.Task(taskReadLater))
            start()
        }
        initCopiedTextProcessor()
        showPrivacyPolicyDialog()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        runtimeRequester?.onActivityResult(resultCode)
    }

    override fun onBackPressed() {
        if (binding.vp.currentItem == 1) {
            super.onBackPressed()
        } else {
            binding.vp.currentItem = 1
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            when (it.actionMasked) {
                MotionEvent.ACTION_DOWN -> BannerAutoSwitchEnableEvent(false).post()
                MotionEvent.ACTION_UP -> BannerAutoSwitchEnableEvent(true).post()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun initCopiedTextProcessor() {
        CopiedTextProcessor.getInstance()
            .setProcessCallback(object : CopiedTextProcessor.ProcessCallback {
                override fun isLink(link: String?) {
                    showCopiedLinkDialog(link ?: "")
                    predefinedTaskQueen.get(taskWanPwd).complete()
                }

                override fun isWanPwd(pwd: WanPwdParser?) {
                    pwd ?: return
                    showWanPwdDialog(pwd)
                    predefinedTaskQueen.get(taskCopiedLink).complete()
                }

                override fun ignored() {
                    predefinedTaskQueen.get(taskCopiedLink).complete()
                    predefinedTaskQueen.get(taskWanPwd).complete()
                }
            })
    }

    private fun showPrivacyPolicyDialog() {
        predefinedTaskQueen.get(taskPrivacyPolicy).runnable { completion ->
            PrivacyPolicyDialog.showIfFirst(viewContext, completion::complete)
        }
    }

    override fun bindData() {
        updateUtils = UpdateUtils.newInstance()
        presenter?.getConfig()
        presenter?.getAdvert()
        presenter?.update()
        window.decorView.postDelayed({
            presenter?.getReadLaterArticle()
        }, 500)
        RecommendManager.getInstance().load()
    }

    override fun onStart() {
        super.onStart()
        binding.vp.postDelayed({
            CopiedTextProcessor.getInstance().process()
        }, 500)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            if (!ThemeUtils.isWillInstall()) {
                ThemeUtils.updateLauncher(applicationContext, ConfigUtils.getInstance().themeName)
            }
        }
    }

    private fun showCopiedLinkDialog(link: String) {
        copiedLinkDialog?.let {
            if (it.link != link) {
                it.dismiss()
                copiedLinkDialog = null
            }
        }
        if (copiedLinkDialog == null) {
            copiedLinkDialog = CopiedLinkDialog(binding.vp, link)
        }
        if (!predefinedTaskQueen.isCompleted) {
            predefinedTaskQueen.get(taskCopiedLink).let {
                if (it.isCompleted) return
                it.runnable { completion ->
                    copiedLinkDialog?.addOnDismissListener(object : Layer.OnDismissListener {
                        override fun onPreDismiss(layer: Layer) {

                        }

                        override fun onPostDismiss(layer: Layer) {
                            completion.complete()
                        }
                    })
                    copiedLinkDialog?.show()
                }
            }
        }
        copiedLinkDialog?.show()
    }

    private fun showWanPwdDialog(parser: WanPwdParser) {
        passwordDialog?.let {
            if (parser != it.password) {
                it.dismiss()
                passwordDialog = null
            }
        }
        if (passwordDialog == null) {
            passwordDialog = PasswordDialog(viewContext, parser)
        }
        if (!predefinedTaskQueen.isCompleted) {
            predefinedTaskQueen.get(taskWanPwd).let {
                if (it.isCompleted) return
                it.runnable { completion ->
                    passwordDialog?.addOnDismissListener(object : Layer.OnDismissListener {
                        override fun onPreDismiss(layer: Layer) {

                        }

                        override fun onPostDismiss(layer: Layer) {
                            passwordDialog = null
                            completion.complete()
                        }
                    })
                    passwordDialog?.show()
                }
            }
        }
        passwordDialog?.show()
    }

    override fun updateSuccess(code: Int, data: UpdateBean?) {
        TODO("Not yet implemented")
    }

    override fun updateFailed(code: Int, msg: String?) {
        TODO("Not yet implemented")
    }

    override fun betaUpdateSuccess(code: Int, data: UpdateBean?) {
        TODO("Not yet implemented")
    }

    override fun betaUpdateFailed(code: Int, msg: String?) {
        TODO("Not yet implemented")
    }

    override fun getConfigSuccess(configBean: ConfigBean?) {
        TODO("Not yet implemented")
    }

    override fun getConfigFailed(code: Int, msg: String?) {
        TODO("Not yet implemented")
    }

    override fun newThemeFounded() {
        TODO("Not yet implemented")
    }

    override fun getAdvertSuccess(code: Int, advertBean: AdvertBean?) {
        TODO("Not yet implemented")
    }

    override fun getAdvertFailed(code: Int, msg: String?) {
        TODO("Not yet implemented")
    }

    override fun getReadLaterArticleSuccess(readLaterModel: ReadLaterModel?) {
        TODO("Not yet implemented")
    }

    override fun getReadLaterArticleFailed() {
        TODO("Not yet implemented")
    }
}