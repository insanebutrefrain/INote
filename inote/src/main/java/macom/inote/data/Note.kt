package macom.inote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 笔记实体类
 */
@Entity(tableName = "notes")
data class Note(
    var title: String,
    var body: String,
    @PrimaryKey val createTime: Long, // 使用 createTime 作为主键
    var modifiedTime: Long
)
