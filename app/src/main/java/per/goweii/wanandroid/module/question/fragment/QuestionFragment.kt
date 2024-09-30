package per.goweii.wanandroid.module.question.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import per.goweii.basic.core.base.BaseFragment
import per.goweii.basic.core.utils.SmartRefreshUtils
import per.goweii.basic.ui.toast.ToastMaker
import per.goweii.basic.utils.listener.SimpleListener
import per.goweii.wanandroid.R
import per.goweii.wanandroid.databinding.FragmentQuestionBinding
import per.goweii.wanandroid.event.CollectionEvent
import per.goweii.wanandroid.event.LoginEvent
import per.goweii.wanandroid.event.ScrollTopEvent
import per.goweii.wanandroid.module.main.adapter.ArticleAdapter
import per.goweii.wanandroid.module.main.model.ArticleListBean
import per.goweii.wanandroid.module.question.presenter.QuestionPresenter
import per.goweii.wanandroid.module.question.view.QuestionView
import per.goweii.wanandroid.utils.MultiStateUtils
import per.goweii.wanandroid.utils.MultiStateUtils.Companion.setEmptyAndErrorClick
import per.goweii.wanandroid.utils.RvScrollTopUtils
import per.goweii.wanandroid.utils.RvScrollTopUtils.ScrollTop

/**
 * @author CuiZhen
 * @date 2020/3/25
 */
class QuestionFragment : BaseFragment<QuestionPresenter,QuestionView>(), QuestionView, ScrollTop {

    companion object {
        private const val PAGE_START = 1

        @JvmStatic
        fun create(): QuestionFragment {
            return QuestionFragment()
        }
    }

    private lateinit var binding: FragmentQuestionBinding

    override fun initRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        mRootView = binding.root
        mViewCreated = true
        return mRootView
    }
    private lateinit var mSmartRefreshUtils: SmartRefreshUtils
    private lateinit var mAdapter: ArticleAdapter

    private var currPage = PAGE_START

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onCollectionEvent(event: CollectionEvent) {
        if (isDetached) {
            return
        }
        if (event.articleId == -1) {
            return
        }
        mAdapter.notifyCollectionEvent(event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginEvent(event: LoginEvent) {
        if (isDetached) {
            return
        }
        if (event.isLogin) {
            currPage = PAGE_START
            presenter!!.getQuestionList(currPage)
        } else {
            mAdapter.notifyAllUnCollect()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onScrollTopEvent(event: ScrollTopEvent) {
        if (isAdded && !isDetached) {
            RvScrollTopUtils.smoothScrollTop(binding.rv)
        }
    }

    override val isRegisterEventBus: Boolean
        get() = true

    override fun setUpPresenter() {
        presenter = QuestionPresenter()
    }

    override fun initViews() {
        mSmartRefreshUtils = SmartRefreshUtils.with(binding.srl)
        mSmartRefreshUtils.pureScrollMode()
        mSmartRefreshUtils.setRefreshListener {
            currPage = PAGE_START
            presenter!!.getQuestionList(currPage)
        }
        binding.rv.layoutManager = LinearLayoutManager(viewContext)
        mAdapter = ArticleAdapter()
        mAdapter.setEnableLoadMore(false)
        mAdapter.setOnLoadMoreListener({
            presenter!!.getQuestionList(currPage)
        }, binding.rv)
        mAdapter.setOnItemChildViewClickListener { _, v, position ->
            mAdapter.getItem(position)?.let { item ->
                if (v.isChecked) {
                    presenter!!.collect(item, v)
                } else {
                    presenter!!.uncollect(item, v)
                }
            }
        }
        binding.rv.adapter = mAdapter
        setEmptyAndErrorClick(binding.msv, SimpleListener {
            MultiStateUtils.toLoading(binding.msv)
            presenter!!.getQuestionList(currPage)
        })
    }

    override fun bindData() {
        MultiStateUtils.toLoading(binding.msv)
        presenter!!.getQuestionListCache(PAGE_START)
    }

    override fun onVisible(isFirstVisible: Boolean) {
        super.onVisible(isFirstVisible)
        if (isFirstVisible) {
            currPage = PAGE_START
            presenter!!.getQuestionList(currPage)
        }
    }

    override fun scrollTop() {
        if (isAdded && !isDetached) {
            RvScrollTopUtils.smoothScrollTop(binding.rv)
        }
    }

    override fun getQuestionListSuccess(code: Int, data: ArticleListBean) {
        currPage = data.curPage + PAGE_START
        if (data.curPage == PAGE_START) {
            MultiStateUtils.toContent(binding.msv)
            mAdapter.setNewData(data.datas)
        } else {
            mAdapter.addData(data.datas)
            mAdapter.loadMoreComplete()
        }
        if (data.isOver) {
            mAdapter.loadMoreEnd()
        } else {
            if (!mAdapter.isLoadMoreEnable) {
                mAdapter.setEnableLoadMore(true)
            }
        }
        mSmartRefreshUtils.success()
    }

    override fun getQuestionListFail(code: Int, msg: String) {
        ToastMaker.showShort(msg)
        mSmartRefreshUtils.fail()
        mAdapter.loadMoreFail()
    }
}