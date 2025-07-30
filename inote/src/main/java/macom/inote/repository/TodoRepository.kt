package macom.inote.repository

import android.util.Log
import macom.inote.data.Todo
import macom.inote.intenet.RetrofitInstance
import macom.inote.room.TodoDao
import retrofit2.await

/**
 * 待办仓库
 */
class TodoRepository(private val todoDao: TodoDao) {
    /**
     * Room数据库操作
     */
    suspend fun insert(todo: Todo): Boolean {
        todoDao.insert(todo)
        return addTodoToServer(todo)  // 同步新增到云端
    }

    suspend fun update(todo: Todo): Boolean {
        todoDao.update(todo)
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
     * 与服务端同步数据
     */
    suspend fun syncAllTodosFromServer(user: String): List<Todo> {
        try {
            val serverTodos = RetrofitInstance.todoApi.getAllTodos(user).await()
            return serverTodos
        } catch (e: Exception) {
            Log.d("TodoRepo", "同步所有待办事项失败", e)
        }
        return listOf()
    }

    suspend fun addTodoToServer(todo: Todo): Boolean {
        try {
            RetrofitInstance.todoApi.createTodo(todo).await()
            return true
        } catch (e: Exception) {
            Log.d("TodoRepo", "同步新增待办事项失败", e)
            return false
        }
    }

    suspend fun updateTodoToServer(todo: Todo): Boolean {
        try {
            RetrofitInstance.todoApi.updateTodo(todo).await()
            return true
        } catch (e: Exception) {
            Log.d("TodoRepo", "同步更新待办事项失败", e)
            return false
        }
    }

    private suspend fun deleteTodoFromServer(todo: Todo): Boolean {
        try {
            RetrofitInstance.todoApi.deleteTodo(todo).await()
            return true
        } catch (e: Exception) {
            Log.d("TodoRepo", "同步删除待办事项失败", e)
            return false
        }
    }
}
