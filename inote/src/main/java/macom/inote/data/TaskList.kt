package macom.inote.data

import androidx.room.Entity

/**
 * 任务表
 */

@Entity(tableName = "task_lists", primaryKeys = ["createTime", "user"])
data class TaskList(
    var listName: String,
    val createTime: Long, // 使用 createTime 作为主键
    var user: String
)



