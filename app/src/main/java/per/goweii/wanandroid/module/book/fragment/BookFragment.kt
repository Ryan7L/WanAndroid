package per.goweii.wanandroid.module.book.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.constant.RefreshState
import per.goweii.basic.core.base.BaseFragment
import per.goweii.basic.core.utils.SmartRefreshUtils
import per.goweii.wanandroid.databinding.FragmentBookBinding
import per.goweii.wanandroid.event.CloseSecondFloorEvent
import per.goweii.wanandroid.module.book.activity.BookDetailsActivity
import per.goweii.wanandroid.module.book.adapter.BookAdapter
import per.goweii.wanandroid.module.book.contract.BookPresenter
import per.goweii.wanandroid.module.book.contract.BookView
import per.goweii.wanandroid.module.book.model.BookBean
import per.goweii.wanandroid.utils.MultiStateUtils
import per.goweii.wanandroid.utils.RvConfigUtils
import per.goweii.wanandroid.widget.refresh.SimpleOnMultiListener

class BookFragment : BaseFragment<BookPresenter, BookView>(), BookView {
    private lateinit var mAdapter: BookAdapter


    override fun setUpPresenter() {
        presenter = BookPresenter()
    }

    private lateinit var binding: FragmentBookBinding
    override fun initRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookBinding.inflate(inflater, container, false)
        mRootView = binding.root
        return mRootView
    }

    override fun initViews() {
        SmartRefreshUtils.with(binding.srl).pureScrollMode()
        binding.srl.setOnMultiListener(object : SimpleOnMultiListener() {
            override fun onFooterMoving(
                footer: RefreshFooter?,
                isDragging: Boolean,
                percent: Float,
                offset: Int,
                footerHeight: Int,
                maxDragHeight: Int
            ) {
                super.onFooterMoving(
                    footer,
                    isDragging,
                    percent,
                    offset,
                    footerHeight,
                    maxDragHeight
                )
                if (binding.srl.state != RefreshState.PullUpCanceled && isDragging && percent > 1.2F) {
                    binding.srl.closeHeaderOrFooter()
                    CloseSecondFloorEvent().post()
                }
            }
        })
        binding.rv.layoutManager = GridLayoutManager(viewContext, 3)
        mAdapter = BookAdapter()
        RvConfigUtils.init(mAdapter)
        mAdapter.setEnableLoadMore(false)
        mAdapter.setOnItemClickListener { _, _, position ->
            mAdapter.getItem(position)?.let { item ->
                BookDetailsActivity.start(requireContext(), item)
            }
        }
        binding.rv.adapter = mAdapter
        MultiStateUtils.setEmptyAndErrorClick(binding.msv) {
            MultiStateUtils.toLoading(binding.msv)
            presenter!!.getList()
        }
    }

    override fun bindData() {
        MultiStateUtils.toLoading(binding.msv)
    }

    override fun onVisible(isFirstVisible: Boolean) {
        super.onVisible(isFirstVisible)
        presenter!!.getList()
    }

    override val isRegisterEventBus: Boolean
        get() = false
    override fun getBookListSuccess(list: List<BookBean>) {
        mAdapter.setNewData(list)
        if (list.isEmpty()) {
            MultiStateUtils.toEmpty(binding.msv, true)
        } else {
            MultiStateUtils.toContent(binding.msv)
        }
    }

    override fun getBookListFailed() {
        MultiStateUtils.toError(binding.msv)
    }
}