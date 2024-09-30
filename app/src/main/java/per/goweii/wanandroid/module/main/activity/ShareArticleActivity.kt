package per.goweii.wanandroid.module.main.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.core.widget.addTextChangedListener
import per.goweii.basic.core.base.BaseActivity
import per.goweii.basic.ui.toast.ToastMaker
import per.goweii.basic.utils.InputMethodUtils
import per.goweii.basic.utils.LogUtils
import per.goweii.basic.utils.listener.OnCustomClickListener
import per.goweii.rxhttp.request.base.BaseBean
import per.goweii.wanandroid.databinding.ActivityShareArticleBinding
import per.goweii.wanandroid.module.main.presenter.ShareArticlePresenter
import per.goweii.wanandroid.module.main.view.ShareArticleView
import per.goweii.wanandroid.utils.UrlOpenUtils
import per.goweii.wanandroid.utils.UserUtils
import per.goweii.wanandroid.utils.web.WebHolder

private const val TAG = "ShareArticleActivity"
class ShareArticleActivity : BaseActivity<ShareArticlePresenter, ShareArticleView>(),
    ShareArticleView {
    private var webHolder: WebHolder? = null
    private lateinit var binding: ActivityShareArticleBinding

    companion object {
        @JvmStatic
        fun start(context: Context, title: String = "", link: String = "") {
            if (!UserUtils.getInstance().doIfLogin(context)) {
                return
            }
            Intent(context, ShareArticleActivity::class.java).runCatching {
                putExtra("title", title)
                putExtra("link", link)
                context.startActivity(this)
            }
        }
    }

    override fun initContentView() {
        binding = ActivityShareArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpPresenter() {
        presenter = ShareArticlePresenter()
    }

    override fun initViews() {
        binding.openBtn.setOnClickListener(this)
        binding.refreshBtn.setOnClickListener(this)
        binding.shareBtn.setOnClickListener(object : OnCustomClickListener() {
            override fun onCustomClick(v: View?) {
                val title = binding.titleEt.text.toString()
                if (TextUtils.isEmpty(title)) {
                    binding.titleEt.requestFocus()
                    return
                }
                val link = binding.linkEt.text.toString()
                if (TextUtils.isEmpty(link)) {
                    binding.linkEt.requestFocus()
                    return
                }
                InputMethodUtils.hide(binding.linkEt)
                presenter?.shareArticle(title, link)
            }
        })
        val link = intent.getStringExtra("link")
        if (!TextUtils.isEmpty(link)) {
            binding.linkEt.setText(link)
        }
        val title = intent.getStringExtra("title")
        if (TextUtils.isEmpty(title)) {
            refreshTitle(link)
        } else {
            resetTitle(title ?: "")
        }
        binding.linkEt.addTextChangedListener(
            afterTextChanged = { str ->
                refreshTitle(str?.toString() ?: "")
            }
        )
    }

    override fun bindData() {

    }

    override fun onResume() {
        super.onResume()
        webHolder?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webHolder?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        webHolder?.onDestroy(true)
    }

    private fun isCorrectUrl(url: String): Boolean {
        if (TextUtils.isEmpty(url)) return false
        val uri = Uri.parse(url) ?: return false
        val scheme = uri.scheme
        if (!TextUtils.equals(scheme, "https") && !TextUtils.equals(scheme, "http")) return false
        return !TextUtils.isEmpty(uri.host)
    }

    private fun refreshTitle(url: String?) {
        LogUtils.i(TAG, "refreshTitle: $url")
        webHolder?.stopLoading()
        if (!isCorrectUrl(url ?: "")) {
            resetTitle()
            return
        }
        if (webHolder == null) {
            webHolder = WebHolder.with(this, binding.webContainer)
                .setOnPageTitleCallback { resetTitle(it) }
                .loadUrl(url)
        } else {
            webHolder?.loadUrl(url)
        }
    }

    private fun resetTitle(title: String = "") {
        if (TextUtils.equals(binding.titleEt.text, title)) return
        binding.titleEt.setText(title)
        binding.titleEt.setSelection(title.length)
    }

    override fun onClickSpace(v: View?) {
        v?.let {
            when (it.id) {
                binding.refreshBtn.id -> refreshTitle(binding.linkEt.text.toString())
                binding.openBtn.id -> {
                    UrlOpenUtils.with(binding.linkEt.text.toString())
                        .title(binding.titleEt.text.toString())
                        .open(viewContext)

                }
            }
        }
    }

    override fun shareArticleSuccess(code: Int, data: BaseBean?) {
        finish()
    }

    override fun shareArticleFailed(code: Int, msg: String?) {
        ToastMaker.showShort(msg)
    }
}