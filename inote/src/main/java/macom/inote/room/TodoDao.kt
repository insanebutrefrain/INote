package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import macom.inote.data.Todo

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)


    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todos WHERE user = :user")
    suspend fun getAllTodos(user: String): List<Todo>

}
