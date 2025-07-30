package macom.inote.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * 任务实体类
 */
@Entity(
    tableName = "tasks",
    primaryKeys = ["createTime", "user"],
    foreignKeys = [
        ForeignKey(
            entity = TaskList::class,  // 引用的父表实体类
            parentColumns = ["createTime", "user"],    // 父表的主键列
            childColumns = ["taskListId", "user"], // 本表的外键列
            onDelete = ForeignKey.CASCADE, // 删除策略
            onUpdate = ForeignKey.CASCADE  // 更新策略
        ),
    ]
)
data class Task(
    var isOver: Boolean,
    var body: String,
    val createTime: Long,
    var remindTime: Long?,
    var overTime: Long? = null,
    var repeatMode: String = "无",
    @ColumnInfo(index = true) // 为外键添加索引提高查询性能
    val taskListId: Long,     // 外键字段
    val user: String
)