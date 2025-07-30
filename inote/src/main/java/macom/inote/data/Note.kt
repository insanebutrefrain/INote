package macom.inote.data

import androidx.room.Entity

/**
 * 笔记实体类
 */
@Entity(tableName = "notes", primaryKeys = ["createTime", "user"])
data class Note(
    var title: String,
    var body: String,
    val createTime: Long, // 使用 createTime 作为主键
    var modifiedTime: Long,
    val user: String
)
