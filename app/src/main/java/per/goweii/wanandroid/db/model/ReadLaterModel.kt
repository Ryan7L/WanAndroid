package per.goweii.wanandroid.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author CuiZhen
 * @date 2020/3/21
 */
@Entity
data class ReadLaterModel(
        @PrimaryKey
        @ColumnInfo(name = "link")
        val link: String,
        @ColumnInfo(name = "title")
        val title: String,
        @ColumnInfo(name = "time")
        val time: Long
)