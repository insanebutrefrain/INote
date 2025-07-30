package macom.inote.repository

import macom.inote.data.User
import macom.inote.room.UserDao

/**
 * 用户仓库
 */
class UserRepository(private val userDao: UserDao) {
    /**
     * Room数据库操作
     */
    suspend fun insert(user: User): Boolean {
        userDao.insert(user)
        return false
    }

    suspend fun update(user: User): Boolean {
        userDao.update(user)
        return false
    }

    suspend fun delete(user: User): Boolean {
        userDao.delete(user)
        return false
    }

    suspend fun findUser(id: String): User? {
        return userDao.findUser(id = id)
    }

    suspend fun findUser(id: String, psw: String): User? {
        return userDao.findUser(id = id, psw = psw)
    }
    /**
     * 与服务端同步 todo
     */
    
}