package per.goweii.wanandroid.event

class ArticleDeleteEvent private constructor(var articleId: Int) : BaseEvent() {
    companion object {
        @JvmStatic
        fun postWithArticleId(articleId: Int) {
            return ArticleDeleteEvent(articleId).post()
        }
    }
}