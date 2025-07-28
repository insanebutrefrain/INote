package macom.inote.room

import android.util.Log
import macom.inote.data.Task
import macom.inote.intenet.RetrofitInstance
import retrofit2.await

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(task: Task): Boolean {
        taskDao.insert(task)
        return addTaskToServer(task)
    }

    suspend fun update(task: Task): Boolean {
        taskDao.update(task)
        return updateTaskToServer(task)
    }

    suspend fun delete(task: Task) {
        taskDao.delete(task)
        deleteTaskFromServer(task)
    }

    suspend fun getAll(): List<Task> {
        val all = taskDao.getAll()
        return taskDao.getAll()
    }

    suspend fun syncAllTasksFromServer(): List<Task> {
        try {
            val serverTasks = RetrofitInstance.taskApi.getAllTasks().await()
            return serverTasks
        } catch (e: Exception) {
            Log.d("TaskRepo", "同步任务失败", e)
        }
        return listOf()
    }

    suspend fun addTaskToServer(task: Task): Boolean {
        try {
            RetrofitInstance.taskApi.createTask(task).await()
            return true
        } catch (e: Exception) {
            Log.d("TaskRepo", "同步新增任务失败", e)
            return false
        }
    }

    suspend fun updateTaskToServer(task: Task): Boolean {
        try {
            RetrofitInstance.taskApi.updateTask(task.createTime, task).await()
            return true
        } catch (e: Exception) {
            Log.d("TaskRepo", "同步更新任务失败", e)
            return false
        }
    }

    private suspend fun deleteTaskFromServer(task: Task): Boolean {
        try {
            RetrofitInstance.taskApi.deleteTask(task.createTime).await()
            return true
        } catch (e: Exception) {
            Log.d("TaskRepo", "同步删除任务失败", e)
            return false
        }
    }
}
