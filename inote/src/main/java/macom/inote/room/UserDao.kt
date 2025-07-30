package macom.inote.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import macom.inote.data.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun findUser(id: String): User?

    @Query("SELECT * FROM User WHERE id = :id AND password = :psw")
    suspend fun findUser(id: String, psw: String): User?
}
