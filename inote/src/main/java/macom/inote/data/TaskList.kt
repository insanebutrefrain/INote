package macom.inote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 任务表
 */

@Entity(tableName = "task_lists")
data class TaskList(
    var listName: String,
    @PrimaryKey val createTime: Long // 使用 createTime 作为主键
)



