package per.goweii.wanandroid.db.model

import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author CuiZhen
 * @date 2020/3/21
 */
@Entity
data class ReadRecordModel(
    @PrimaryKey
    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "time")
    val time: Long,
    @ColumnInfo(name = "lastTime")
    val lastTime: Long,
    @IntRange(from = 0, to = 10000) val percent: Int,
) {
    companion object {
        const val MIN_PERCENT = 0
        const val MAX_PERCENT = 10000
    }

    val percentFloat: Float
        @FloatRange(from = 0.0, to = 1.0)
        get() = (percent.toFloat() / MAX_PERCENT.toFloat()).coerceIn(0f, 1f)
}