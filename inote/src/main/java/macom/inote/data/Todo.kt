package macom.inote.data


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    var isOver: Boolean,
    var body: String,
    @PrimaryKey val createTime: Long, // 使用 createTime 作为主键
    var remindTime: Long?
)
