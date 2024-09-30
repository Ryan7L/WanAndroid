package per.goweii.wanandroid.common

class Config {
    companion object {
        /**
         * 返回列表顶部双击超时
         */
        const val SCROLL_TOP_DOUBLE_CLICK_DELAY: Long = 500L

        /**
         * 阅读历史最大保存个数
         */
        const val READ_RECORD_MAX_COUNT: Int = 1000

        /**
         * 消息通知最大显示个数，超过显示99+
         */
        const val NOTIFICATION_MAX_SHOW_COUNT: Int = 99
    }
}