package macom.inote.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 任务实体类
 */
@Entity(tableName = "tasks")
data class Task(
    var isOver: Boolean,
    var body: String,
    @PrimaryKey val createTime: Long, // 使用 createTime 作为主键
    var remindTime: Long?,
    val taskListId: Long // 外键，指向 TaskList
)
