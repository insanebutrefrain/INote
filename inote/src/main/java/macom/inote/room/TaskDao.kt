package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import macom.inote.data.Task

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)


    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE user = :user ")
    suspend fun getAll(user: String): List<Task>
}
