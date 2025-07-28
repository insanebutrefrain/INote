package macom.inote.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import macom.inote.data.Note
import macom.inote.data.TaskList
import macom.inote.room.AppDatabase
import macom.inote.room.NoteRepository
import macom.inote.room.TaskListRepository
import macom.inote.room.TaskRepository
import macom.inote.room.TodoRepository

open class INoteViewModel(application: Application) : AndroidViewModel(application) {
    private val _state =
        MutableStateFlow(INoteState())
    val state = _state.asStateFlow()
    private val taskRepository = TaskRepository(AppDatabase.getDatabase(application).taskDao())
    private val todoRepository = TodoRepository(AppDatabase.getDatabase(application).todoDao())
    private val noteRepository = NoteRepository(AppDatabase.getDatabase(application).noteDao())
    private val taskListRepository =
        TaskListRepository(AppDatabase.getDatabase(application).taskListDao())

    init {
        viewModelScope.launch {
            _state.value.notes.addAll(noteRepository.getAll())
            _state.value.tasks.addAll(taskRepository.getAll())
            _state.value.taskLists.addAll(taskListRepository.getAll())
            _state.value.todos.addAll(todoRepository.getAll())
        }
    }

    /**
     * 同步
     */
    suspend fun syncNotes(): Boolean {
        return try {
            withContext(Dispatchers.IO) {

                // 从云端获取所有笔记并同步到本地
                val allInServer = noteRepository.getAllNotesFromServer()
                allInServer.forEach {
                    val success = noteRepository.insert(it)
                    if (!success) {
                        Log.d("网络", "同步本地笔记失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }

                // 获取本地所有笔记并同步到云端
                val all = noteRepository.getAll()
                all.forEach {
                    val success = noteRepository.addNoteToServer(it)
                    if (!success) {
                        Log.d("网络", "同步笔记到云端失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }
                // 更新本地状态
                _state.value.notes.clear()
                _state.value.notes.addAll(noteRepository.getAll())
                true // 如果所有操作成功，则返回 true
            }
        } catch (e: Exception) {
            Log.d("网络", "同步笔记失败，异常: ${e.message}")
            false // 捕获到异常时返回 false
        }
    }

    suspend fun syncTodos(): Boolean {
        return try {
            withContext(Dispatchers.IO) {

                // 从云端获取所有笔记并同步到本地
                val allInServer = todoRepository.syncAllTodosFromServer()
                allInServer.forEach {
                    val success = todoRepository.insert(it)
                    if (!success) {
                        Log.d("网络", "同步本地待办失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }

                // 获取本地所有笔记并同步到云端
                val all = todoRepository.getAll()
                all.forEach {
                    val success = todoRepository.addTodoToServer(it)
                    if (!success) {
                        Log.d("网络", "同步待办到云端失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }
                // 更新本地状态
                _state.value.todos.clear()
                _state.value.todos.addAll(todoRepository.getAll())
                true // 如果所有操作成功，则返回 true
            }
        } catch (e: Exception) {
            Log.d("网络", "同步待办失败，异常: ${e.message}")
            false // 捕获到异常时返回 false
        }
    }

    suspend fun syncTasks(): Boolean {
        return try {
            withContext(Dispatchers.IO) {

                // 从云端获取所有笔记并同步到本地
                val allTaskInServer = taskRepository.syncAllTasksFromServer()
                allTaskInServer.forEach {
                    val success = taskRepository.insert(it)
                    if (!success) {
                        Log.d("网络", "同步本地任务失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }
                taskListRepository.syncAllTaskListsFromServer().forEach {
                    val success = taskListRepository.insert(it)
                    if (!success) {
                        Log.d("网络", "同步本地任务表失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }
                // 更新本地状态
                // 获取本地所有笔记并同步到云端
                val allTasks = taskRepository.getAll()
                allTasks.forEach {
                    val success = taskRepository.addTaskToServer(it)
                    if (!success) {
                        Log.d("网络", "同步任务到云端失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }
                taskListRepository.getAll().forEach {
                    val success = taskListRepository.addTaskListToServer(it)
                    if (!success) {
                        Log.d("网络", "同步任务表到云端失败: ${it}")
                        return@withContext false // 立即返回 false，停止后续逻辑
                    }
                }
                _state.value.tasks.clear()
                _state.value.taskLists.clear()
                _state.value.tasks.addAll(taskRepository.getAll())
                _state.value.taskLists.add(TaskList("全部", 0))
                _state.value.taskLists.addAll(taskListRepository.getAll())
                true // 如果所有操作成功，则返回 true
            }
        } catch (e: Exception) {
            Log.d("网络", "同步失败，异常: ${e.message}")
            false // 捕获到异常时返回 false
        }
    }


    /**
     * note
     */
    fun addNote(intent: INoteIntent.AddNote) {
        val newValue = _state.value.copy()
        newValue.notes.add(intent.note)
        _state.value = newValue
        viewModelScope.launch {
            noteRepository.insert(intent.note)
        }
    }

    fun editNote(intent: INoteIntent.EditNote) {
        val index = _state.value.notes.indexOfFirst { it.createTime == intent.createTime }
        val newNote = Note(
            title = intent.title,
            body = intent.body,
            createTime = intent.createTime,
            modifiedTime = intent.modifiedTime
        )
        val newState = state.value.copy()
        if (index != -1) {
            newState.notes[index] = newNote
            _state.value = newState
            viewModelScope.launch {
                noteRepository.update(newNote)
            }
        } else {
            newState.notes.add(newNote)
            _state.value = newState
            viewModelScope.launch {
                noteRepository.insert(newNote)
            }
        }

    }

    fun deleteNote(intent: INoteIntent.DeleteNote) {
        val newValue = _state.value.copy()
        newValue.notes.remove(intent.note)
        _state.value = newValue

        viewModelScope.launch {
            noteRepository.delete(intent.note)
        }
    }

    /**
     * todo
     */
    fun addTodo(intent: INoteIntent.AddToDO) {
        _state.value.todos.add(intent.todo)
        _state.value = _state.value

        viewModelScope.launch {
            todoRepository.insert(intent.todo)
        }
    }

    fun editTodo(intent: INoteIntent.EditTodo) {
        val index = _state.value.todos.indexOf(intent.todo)
        val newTodo = intent.todo.copy(body = intent.newBody)
        if (index != -1) {
            _state.value.todos[index] = newTodo
        }

        viewModelScope.launch {
            todoRepository.update(newTodo)
        }
    }

    fun checkTodo(intent: INoteIntent.CheckTodo) {
        val index = _state.value.todos.indexOf(intent.todo)
        val newTodo = intent.todo.copy(isOver = !intent.todo.isOver)
        if (index != -1) {
            // 更新 SnapshotStateList
            _state.value.todos[index] = newTodo
            viewModelScope.launch {
                todoRepository.update(newTodo)
            }
        }
    }

    fun deleteTodo(intent: INoteIntent.DeleteTodo) {
        val newValue = _state.value.copy()
        newValue.todos.remove(intent.todo)
        _state.value = newValue

        viewModelScope.launch {
            todoRepository.delete(intent.todo)
        }
    }

    /**
     * task lists
     */
    fun addTaskList(intent: INoteIntent.AddTaskList) {
        val taskLists = _state.value.copy().taskLists
        taskLists.add(intent.taskList)
        _state.value.taskLists = taskLists
        _state.value = _state.value

        viewModelScope.launch {
            taskListRepository.insert(intent.taskList)
        }
    }

    fun editTaskListName(intent: INoteIntent.EditTaskListName) {
        val newValue = _state.value.copy()
        val index = newValue.taskLists.indexOfFirst { it == intent.nowTaskList }
        if (index != -1) {
            val newTaskList = intent.nowTaskList.copy(listName = intent.listName)
            newValue.taskLists[index] = newTaskList
            _state.value = newValue
        }
        // 这里可能有问题
        viewModelScope.launch {
            taskListRepository.update(intent.nowTaskList)
        }
    }

    fun deleteTaskList(intent: INoteIntent.DeleteTaskList) {
        val newValue = _state.value.copy()
        val newLists = newValue.taskLists
        for (i in 0..<newLists.size) {
            if (newLists[i].createTime == intent.taskList.createTime) {
                newLists.remove(newLists[i])
                break
            }
        }
        newValue.taskLists = newLists
        _state.value = newValue
        viewModelScope.launch { taskListRepository.delete(intent.taskList) }
    }

    /**
     * Task
     */
    fun addTask(intent: INoteIntent.AddTask) {
        val newValue = _state.value.copy()
        val newTasks = newValue.tasks
        newTasks.add(intent.newTask)
        _state.value = newValue

        viewModelScope.launch {
            taskRepository.insert(intent.newTask)
        }
    }

    fun checkTask(intent: INoteIntent.CheckTask) {
        val index = _state.value.tasks.indexOf(intent.task)
        val newTask = intent.task.copy(isOver = !intent.task.isOver)
        if (index != -1) {
            _state.value.tasks[index] = newTask
        }
        viewModelScope.launch {
            taskRepository.update(newTask)
        }
    }

    fun editTask(intent: INoteIntent.EditTask) {
        val index = _state.value.tasks.indexOf(intent.task)
        val newTask = intent.task.copy(body = intent.newBody)
        if (index != -1) {
            _state.value.tasks[index] = newTask
        }
        viewModelScope.launch {
            taskRepository.update(newTask)
        }
    }

    fun deleteTask(intent: INoteIntent.DeleteTask) {
        val newValue = _state.value.copy()
        newValue.tasks.remove(intent.task)
        _state.value = newValue

        viewModelScope.launch {
            taskRepository.delete(intent.task)
        }
    }
}