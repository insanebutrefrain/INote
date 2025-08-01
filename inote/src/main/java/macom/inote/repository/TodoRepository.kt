package macom.inote.repository

import android.util.Log
import macom.inote.data.Todo
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.TodoDao

/**
 * 待办仓库
 */
class TodoRepository(private val todoDao: TodoDao) {
    /**
     * Room数据库操作，同时发出服务端请求，网络超时返回false
     */
    suspend fun insert(todo: Todo): Boolean {
        todoDao.insert(todo)
        return addTodoToServer(todo)  // 同步新增到云端
    }

    suspend fun update(todo: Todo): Boolean {
        todoDao.insert(todo)
        return updateTodoToServer(todo)  // 同步更新到云端
    }

    suspend fun delete(todo: Todo) {
        todoDao.delete(todo)
        deleteTodoFromServer(todo)  // 删除云端数据
    }

    suspend fun getAll(user: String): List<Todo> {
        val all = todoDao.getAllTodos(user)
        return all
    }

    /**
     * 与服务端同步数据,请求单个数据不报异常，同步所有数据时会上报网络异常
     */
    suspend fun syncAllTodo(user: String): Boolean {
        try {
            todoDao.getAllTodos(user).forEach {
                RetrofitInstance.todoApi.updateTodo(it)
            }
            RetrofitInstance.todoApi.getAllTodos(user).forEach {
                todoDao.insert(it)
            }
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步所有待办事项失败", e)
            return false
        }
    }

    suspend fun addTodoToServer(todo: Todo): Boolean {
        try {
            RetrofitInstance.todoApi.createTodo(todo)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步新增待办事项失败", e)
            return false
        }
    }

    suspend fun updateTodoToServer(todo: Todo): Boolean {
        try {
            RetrofitInstance.todoApi.updateTodo(todo)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步更新待办事项失败", e)
            return false
        }
    }

    private suspend fun deleteTodoFromServer(todo: Todo): Boolean {
        try {
            RetrofitInstance.todoApi.deleteTodo(todo)
            return true
        } catch (e: Exception) {
            Log.d("网络", "同步删除待办事项失败", e)
            return false
        }
    }
}
