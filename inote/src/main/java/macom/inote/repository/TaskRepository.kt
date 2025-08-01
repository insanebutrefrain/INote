package macom.inote.repository

import android.util.Log
import macom.inote.data.Task
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.TaskDao

/**
 * 任务表仓库
 */
class TaskRepository(private val taskDao: TaskDao) {

    /**
     * 本地Room数据库操作，同时发出服务端请求，网络超时返回false
     */
    suspend fun insert(task: Task): Boolean {
        taskDao.insert(task)
        return addTaskToServer(task)
    }

    suspend fun update(task: Task): Boolean {
        taskDao.insert(task)
        return updateTaskToServer(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
        deleteTaskFromServer(task)
    }

    suspend fun getAll(user: String): List<Task> {
        return taskDao.getAll(user)
    }

    /**
     * 向服务端请求,请求单个数据不报异常，同步所有数据时会上报网络异常
     */

    suspend fun syncAllTasks(user: String): Boolean {
        try {
            taskDao.getAll(user).forEach {
                RetrofitInstance.taskApi.updateTask(it)
            }
            RetrofitInstance.taskApi.getAllTasks(user).forEach {
                taskDao.insert(it)
            }
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步所有任务失败", e)
            return false
        }
    }

    suspend fun addTaskToServer(task: Task): Boolean {
        try {
            Log.d("我",task.toString())
            RetrofitInstance.taskApi.createTask(task)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步新增任务失败", e)
            return false
        }
    }

    suspend fun updateTaskToServer(task: Task): Boolean {
        try {
            Log.d("我",task.toString())
            RetrofitInstance.taskApi.updateTask(task)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步更新任务失败", e)
            return false
        }
    }

    private suspend fun deleteTaskFromServer(task: Task): Boolean {
        try {
            RetrofitInstance.taskApi.deleteTask(task)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步删除任务失败", e)
            return false
        }
    }
}
