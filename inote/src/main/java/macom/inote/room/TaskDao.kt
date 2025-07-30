package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import macom.inote.data.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE taskListId = :taskListId")
    suspend fun getTasksByListId(taskListId: Long): List<Task>

    @Query("SELECT * FROM tasks WHERE user = :user ")
    suspend fun getAll(user: String): List<Task>
}
