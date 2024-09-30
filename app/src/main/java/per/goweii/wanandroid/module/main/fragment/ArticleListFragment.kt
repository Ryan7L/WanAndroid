package per.goweii.wanandroid.module.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import per.goweii.basic.core.base.BaseFragment
import per.goweii.wanandroid.R
import per.goweii.wanandroid.module.main.contract.ArticleListPresenter
import per.goweii.wanandroid.module.main.contract.ArticleListView

/**
 * @author CuiZhen
 * @date 2020/3/22
 */
class ArticleListFragment : BaseFragment<ArticleListPresenter, ArticleListView>(), ArticleListView {


    override fun initRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_article_list, container, false)
    }

    override fun setUpPresenter() {
        presenter = ArticleListPresenter()
    }

    override fun initViews() {
    }

    override fun bindData() {
    }
}