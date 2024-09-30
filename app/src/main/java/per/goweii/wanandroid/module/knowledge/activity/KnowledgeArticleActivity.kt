package per.goweii.wanandroid.module.knowledge.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import per.goweii.basic.core.adapter.MultiFragmentPagerAdapter
import per.goweii.basic.core.base.BaseActivity
import per.goweii.wanandroid.common.Config
import per.goweii.wanandroid.databinding.ActivityKnowledgeArticleBinding
import per.goweii.wanandroid.event.ScrollTopEvent
import per.goweii.wanandroid.module.knowledge.fragment.KnowledgeArticleFragment
import per.goweii.wanandroid.module.knowledge.presenter.KnowledgePresenter
import per.goweii.wanandroid.module.knowledge.view.KnowledgeView
import per.goweii.wanandroid.module.main.model.ArticleBean
import per.goweii.wanandroid.module.main.model.ChapterBean
import per.goweii.wanandroid.utils.MagicIndicatorUtils
import per.goweii.wanandroid.utils.MultiStateUtils

class KnowledgeArticleActivity : BaseActivity<KnowledgePresenter, KnowledgeView>(), KnowledgeView {
    private var lastClickTime = 0L
    private var lastClickPos = 0
    private lateinit var adapter: MultiFragmentPagerAdapter<ChapterBean, KnowledgeArticleFragment>
    private lateinit var commonNavigator: CommonNavigator
    private var superChapterId = 0
    private var chapterId = 0
    private lateinit var binding: ActivityKnowledgeArticleBinding

    companion object {
        @JvmStatic
        fun start(context: Context, chapterBean: ChapterBean, currPos: Int) {
            Intent(context, KnowledgeArticleActivity::class.java).runCatching {
                putExtra("chapterBean", chapterBean)
                putExtra("currPos", currPos)
                context.startActivity(this)
            }
        }

        @JvmStatic
        fun start(context: Context, superChapterId: Int, superChapterName: String, chapterId: Int) {
            Intent(context, KnowledgeArticleActivity::class.java).runCatching {
                putExtra("superChapterName", superChapterName)
                putExtra("superChapterId", superChapterId)
                putExtra("chapterId", chapterId)
                context.startActivity(this)
            }
        }

        @JvmStatic
        fun start(context: Context, tag: ArticleBean.TagsBean?) {
            tag ?: return
            val url = tag.url
            if (url.isNullOrEmpty()) return
            // /wxarticle/list/410/1
            // /article/list/0?cid=440
            // /project/list/1?cid=367
            // wana://www.wanandroid.com/wxarticle/list/410/1?cid=440
            val uri = Uri.parse(url)
            runCatching {
                var cid: String? = uri.getQueryParameter("cid") ?: ""
                if (cid!!.isEmpty()) {
                    val path = uri.pathSegments
                    if (path != null && path.size >= 3) {
                        cid = path[2]
                    }
                }
                var chapterId = 0
                if (cid != null) {
                    chapterId = cid.toInt()
                }
                if (chapterId > 0) {
                    start(context, 0, "", chapterId)
                }
            }
        }

    }

    override fun initContentView() {
        binding = ActivityKnowledgeArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun setUpPresenter() {
        presenter = KnowledgePresenter()
    }

    override fun initViews() {
        binding.abc.titleTextView.setOnClickListener {
            notifyScrollTop(binding.vp.currentItem)
        }
        adapter = MultiFragmentPagerAdapter(supportFragmentManager,
            object :
                MultiFragmentPagerAdapter.FragmentCreator<ChapterBean, KnowledgeArticleFragment> {
                override fun create(data: ChapterBean, pos: Int): KnowledgeArticleFragment {
                    return KnowledgeArticleFragment.create(data, pos)
                }

                override fun getTitle(data: ChapterBean): String {
                    return data.name
                }
            })
        binding.vp.adapter = adapter
        commonNavigator = MagicIndicatorUtils.commonNavigator(
            binding.mi,
            binding.vp,
            adapter,
            this::notifyScrollTop
        )
    }

    private fun notifyScrollTop(position: Int) {
        val currentClickTime = System.currentTimeMillis()
        if (lastClickPos == position &&
            currentClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY
        ) {
            ScrollTopEvent(KnowledgeArticleFragment::class.java, binding.vp.currentItem).post()
        }
        lastClickPos = position
        lastClickTime = currentClickTime
    }

    override fun bindData() {
        val bean = intent.getSerializableExtra("chapterBean") as? ChapterBean
        val currPos = intent.getIntExtra("currPos", 0)
        bean?.let {
            initVp(bean, currPos)
        } ?: {
            MultiStateUtils.toLoading(binding.msv)
            superChapterId = intent.getIntExtra("superChapterId", -1)
            chapterId = intent.getIntExtra("chapterId", -1)
            if (superChapterId <= 0 && chapterId <= 0) {
                MultiStateUtils.toEmpty(binding.msv)
            } else {
                val superChapterName = intent.getStringExtra("superChapterName")
                binding.abc.titleTextView.text = superChapterName
                presenter?.getKnowledgeListCacheAndNet()
            }
        }
    }

    private fun initVp(bean: ChapterBean, currPos: Int) {
        MultiStateUtils.toContent(binding.msv)
        binding.abc.titleTextView.text = bean.name
        adapter.setDataList(bean.children)
        commonNavigator.notifyDataSetChanged()
        binding.vp.setCurrentItem(currPos, false)
    }

    override fun getKnowledgeListSuccess(code: Int, data: List<ChapterBean>) {
        val (superChapter, position) = findSuperChapterAndPosition(data)

        if (superChapter != null) {
            initVp(superChapter, position)
        } else {
            MultiStateUtils.toError(binding.msv)
        }
    }

    private fun findSuperChapterAndPosition(data: List<ChapterBean>): Pair<ChapterBean?, Int> {
        // 1. 尝试直接查找父章节
        val superChapter = data.find { it.id == superChapterId }
        if (superChapter != null) {
            val position = superChapter.children.indexOfFirst { it.id == chapterId }
            return superChapter to position
        }

        // 2. 如果未找到，则遍历子章节查找
        for (chapter in data) {
            val position = chapter.children.indexOfFirst { it.id == chapterId }
            if (position != -1) {
                return chapter to position
            }
        }

        // 3. 未找到匹配的章节
        return null to -1
    }


    override fun getKnowledgeListFail(code: Int, msg: String?) {
        MultiStateUtils.toError(binding.msv)
    }
}
