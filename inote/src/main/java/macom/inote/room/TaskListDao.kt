package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import macom.inote.data.TaskList

@Dao
interface TaskListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskList: TaskList)


    @Delete
    suspend fun delete(taskList: TaskList)

    @Query("SELECT * FROM task_lists WHERE user = :user")
    suspend fun getAll(user: String): List<TaskList>
}
