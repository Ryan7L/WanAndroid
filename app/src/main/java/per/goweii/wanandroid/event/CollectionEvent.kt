package per.goweii.wanandroid.event

class CollectionEvent private constructor(var isCollect: Boolean,var articleId: Int,var collectId: Int): BaseEvent(){

    companion object{
        @JvmStatic
        fun postCollectWithCollectId(collectId: Int) {
            CollectionEvent(true, -1, collectId).post()
        }
        @JvmStatic
        fun postCollectWithArticleId(articleId: Int) {
            CollectionEvent(true, articleId, -1).post()
        }
        @JvmStatic
        fun postUnCollectWithArticleId(articleId: Int) {
            CollectionEvent(false, articleId, -1).post()
        }
        @JvmStatic
        fun postUncollectWithCollectId(collectId: Int) {
            CollectionEvent(false, -1, collectId).post()
        }
        @JvmStatic
        fun postUncollect(articleId: Int, collectId: Int) {
            CollectionEvent(false, articleId, collectId).post()
        }

    }

}