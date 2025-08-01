package macom.inote.data


import androidx.room.Entity

/**
 * 代办实体类
 */

@Entity(tableName = "todos", primaryKeys = ["createTime", "user"])
data class Todo(
    var isOver: Boolean,
    var body: String,
    val createTime: Long, // 使用 createTime 作为主键
    var remindTime: Long?,
    var overTime: Long? = null,
    var repeatPeriod: Long = 0,
    var user: String
)
