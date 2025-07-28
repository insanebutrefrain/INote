package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import macom.inote.data.TaskList

@Dao
interface TaskListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskList: TaskList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(taskList: TaskList)

    @Delete
    suspend fun delete(taskList: TaskList)

    @Query("SELECT * FROM task_lists")
    suspend fun getAll(): List<TaskList>

    @Query("SELECT * FROM task_lists WHERE createTime = :createTime")
    suspend fun getTaskListByCreateTime(createTime: Long): TaskList?
}
