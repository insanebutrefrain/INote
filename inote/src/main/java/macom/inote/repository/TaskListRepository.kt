package macom.inote.repository

import android.util.Log
import macom.inote.data.TaskList
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.TaskListDao
import retrofit2.await

/**
 * 任务表仓库
 */
class TaskListRepository(private val taskListDao: TaskListDao) {

    /**
     * 本地Room数据库操作
     */
    suspend fun insert(taskList: TaskList): Boolean {
        taskListDao.insert(taskList)
        return addTaskListToServer(taskList)  // 同步新增到云端

    }

    suspend fun update(taskList: TaskList): Boolean {
        taskListDao.update(taskList)
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
     * 向服务端请求
     */
    suspend fun syncAllTaskListsFromServer(user: String): List<TaskList> {
        try {
            val serverTaskLists = RetrofitInstance.taskListApi.getAllTaskLists(user).await()

            return serverTaskLists
        } catch (e: Exception) {
            Log.d("TaskListRepo", "同步所有任务表失败")
        }
        return listOf()
    }

    suspend fun addTaskListToServer(taskList: TaskList): Boolean {
        try {
            RetrofitInstance.taskListApi.createTaskList(taskList).await()
            return true
        } catch (e: Exception) {
            Log.d("TaskListRepo", "同步新增任务表失败")
            return false
        }
    }

    suspend fun updateTaskListToServer(taskList: TaskList): Boolean {
        try {
            RetrofitInstance.taskListApi.updateTaskList(taskList).await()
            return true
        } catch (e: Exception) {
            Log.d("TaskListRepo", "同步更新任务列表失败")
            return false
        }
    }

    private suspend fun deleteTaskListFromServer(taskList: TaskList): Boolean {
        try {
            RetrofitInstance.taskListApi.deleteTaskList(taskList).await()
            return true
        } catch (e: Exception) {
            Log.d("TaskListRepo", "同步删除任务列表失败", e)
            return false
        }
    }
}
