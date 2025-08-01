package macom.inote.repository

import android.util.Log
import macom.inote.data.User
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.UserDao

/**
 * 用户仓库
 */
class UserRepository(private val userDao: UserDao) {
    /**
     * Room数据库操作，同时发出服务端请求，网络超时返回false
     */
    suspend fun insert(user: User): Boolean {
        userDao.insert(user)
        return addUserToServer(user)
    }

    suspend fun update(user: User): Boolean {
        userDao.insert(user)
        return updateUserToServer(user)
    }

    suspend fun delete(user: User): Boolean {
        userDao.delete(user)
        return deleteUserFromServer(user)
    }

    suspend fun find(id: String): User? {
        val res = userDao.find(id = id)
        if (res != null)
            return res

        return findUserFromServer(id)
    }

    suspend fun find(id: String, psw: String): User? {
        val res = userDao.find(id = id, psw = psw)
        if (res != null)
            return res
        return findUserFromServer(id = id, psw = psw)
    }

    /**
     * 与服务端同步,请求单个数据不报异常，同步所有数据时会上报网络异常
     */

    suspend fun syncAllUser(): Boolean {
        try {
            userDao.getAll().forEach {
                RetrofitInstance.userApi.updateUser(it)
            }

            RetrofitInstance.userApi.getAllUser()
                .forEach {
                    userDao.insert(it)
                }
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步用户数据失败", e)
            return false
        }
    }

    suspend fun addUserToServer(user: User): Boolean {
        try {
            RetrofitInstance.userApi.addUser(user)
            return true
        } catch (e: Exception) {
            Log.d("网络", "新增服务端用户失败", e)
            return false
        }
    }

    suspend fun deleteUserFromServer(user: User): Boolean {
        try {
            RetrofitInstance.userApi.deleteUser(user)
            return true
        } catch (e: Exception) {
            Log.d("网络", "删除服务端用户失败", e)
            return false
        }
    }

    suspend fun updateUserToServer(user: User): Boolean {
        try {
            RetrofitInstance.userApi.updateUser(user)
            return true
        } catch (e: Exception) {
            Log.d("网络", "修改服务端用户失败", e)
            return false
        }
    }

    suspend fun findUserFromServer(id: String): User? {
        try {
            return RetrofitInstance.userApi.findUser(id)
        } catch (e: Exception) {
            Log.d("网络", "查询服务端用户失败(id)", e)
            return null
        }
    }

    suspend fun findUserFromServer(id: String, psw: String): User? {
        try {
            return RetrofitInstance.userApi.findUser(id = id, psw = psw)
        } catch (e: Exception) {
            Log.d("网络", "查询服务端用户失败(id,psw)", e)
            return null
        }
    }
}