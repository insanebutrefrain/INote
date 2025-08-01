package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import macom.inote.data.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun find(id: String): User?

    @Query("SELECT * FROM User WHERE id = :id AND password = :psw")
    suspend fun find(id: String, psw: String): User?

    @Query("SELECT * FROM User")
    suspend fun getAll(): List<User>


}
