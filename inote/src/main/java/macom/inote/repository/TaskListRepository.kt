package macom.inote.repository

import android.util.Log
import macom.inote.data.TaskList
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.TaskListDao

/**
 * 任务表仓库
 */
class TaskListRepository(private val taskListDao: TaskListDao) {

    /**
     * 本地Room数据库操作，同时发出服务端请求，网络超时返回false
     */
    suspend fun insert(taskList: TaskList): Boolean {
        taskListDao.insert(taskList)
        return addTaskListToServer(taskList)  // 同步新增到云端

    }

    suspend fun update(taskList: TaskList): Boolean {
        taskListDao.insert(taskList)
        return updateTaskListToServer(taskList)  // 同步更新到云端
    }

    suspend fun delete(taskList: TaskList) {
        taskListDao.delete(taskList)
        deleteTaskListFromServer(taskList)  // 删除云端数据
    }

    suspend fun getAll(user: String): List<TaskList> {
        val all = taskListDao.getAll(user)
        return all
    }

    /**
     * 向服务端请求,单个请求不报异常，整个同步时网络异常报错
     */
    suspend fun syncTaskList(user: String): Boolean {
        try {
            taskListDao.getAll(user).forEach {
                RetrofitInstance.taskListApi.updateTaskList(it)
            }
            RetrofitInstance.taskListApi.getAllTaskLists(user)
                .forEach { taskListDao.insert(it) }
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步所有任务表失败", e)
            return false
        }
    }

    suspend fun addTaskListToServer(taskList: TaskList): Boolean {
        try {
            RetrofitInstance.taskListApi.createTaskList(taskList)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步新增任务表失败", e)
            return false
        }
    }

    suspend fun updateTaskListToServer(taskList: TaskList): Boolean {
        try {
            RetrofitInstance.taskListApi.updateTaskList(taskList)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步更新任务列表失败",e)
            return false
        }
    }

    private suspend fun deleteTaskListFromServer(taskList: TaskList): Boolean {
        try {
            RetrofitInstance.taskListApi.deleteTaskList(taskList)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步删除任务列表失败", e)
            return false
        }
    }
}
