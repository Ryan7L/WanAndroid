package per.goweii.wanandroid.module.book.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import per.goweii.wanandroid.R
import per.goweii.wanandroid.module.book.model.BookBean
import per.goweii.wanandroid.utils.ImageLoader

class BookAdapter: BaseQuickAdapter<BookBean,BaseViewHolder>(R.layout.rv_item_book) {
    override fun convert(helper: BaseViewHolder, item: BookBean?) {
        ImageLoader.image(helper.getView(R.id.piv_img),item?.cover)
    }
}