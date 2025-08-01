package macom.inote.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import macom.inote.data.Note
import macom.inote.data.TaskList
import macom.inote.repository.NoteRepository
import macom.inote.repository.TaskListRepository
import macom.inote.repository.TaskRepository
import macom.inote.repository.TodoRepository
import macom.inote.repository.UserRepository
import macom.inote.room.AppDatabase
import macom.inote.workerManager.ReminderScheduler

open class INoteViewModel(application: Application) : AndroidViewModel(application) {
    private val _state = MutableStateFlow(INoteState())
    val state = _state.asStateFlow()
    private val taskRepository = TaskRepository(AppDatabase.getDatabase(application).taskDao())
    private val todoRepository = TodoRepository(AppDatabase.getDatabase(application).todoDao())
    private val noteRepository = NoteRepository(AppDatabase.getDatabase(application).noteDao())
    private val taskListRepository =
        TaskListRepository(AppDatabase.getDatabase(application).taskListDao())
    private val userRepository = UserRepository(AppDatabase.getDatabase(application).userDao())

    private var loginInfo: SharedPreferences =
        application.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)

    val reminderScheduler = ReminderScheduler(application.applicationContext)

    /**
     * 调度提醒
     */
    // 封装调度提醒的方法
    fun scheduleNoteReminder(
        id: String,
        title: String,
        content: String,
        repeatPeriod: Long,
        delayInMillis: Long
    ) {
        reminderScheduler.scheduleReminder(id, title, content, repeatPeriod, delayInMillis)
    }

    // 封装取消提醒的方法
    fun cancelNoteReminder(noteId: String) {
        reminderScheduler.cancelReminder(noteId)
    }


    init {
        // 获取所有数据
        reload()
    }

    private fun reload() {
        if (getUser() != null && getPsw() != null) {
            viewModelScope.launch {
                _state.value.user.value = userRepository.find(getUser()!!, getPsw()!!)
                _state.value.notes.run { clear();addAll(noteRepository.getAll(getUser()!!)) }
                _state.value.tasks.run { clear();addAll(taskRepository.getAll(getUser()!!)) }
                _state.value.taskLists.run { clear();addAll(taskListRepository.getAll(getUser()!!)) }
                _state.value.todos.run { clear();addAll(todoRepository.getAll(getUser()!!)) }
            }
        }
    }

    /**
     * user
     */

    // 注册
    suspend fun register(intent: INoteIntent.Register): Boolean {
        return if (userRepository.find(intent.user.id) != null) false else {
            // 注册自带全部任务表
            userRepository.insert(intent.user)
            taskListRepository.insert(
                TaskList(
                    listName = "全部", createTime = 0, user = intent.user.id
                )
            )
            true
        }
    }

    suspend fun login(intent: INoteIntent.Login): Boolean {
        if (userRepository.find(intent.id, intent.psw) == null) {
            return false
        } else {
            setUserAndPsw(intent.id, intent.psw)
            reload()
            return true
        }
    }

    fun logout(intent: INoteIntent.Logout) {
        setUserAndPsw(user = null, psw = null)
    }

    /**
     * 登录信息
     */

    fun getUser(): String? {
        return loginInfo.getString("user", null)
    }

    fun getPsw(): String? {
        return loginInfo.getString("psw", null)
    }

    private fun setUserAndPsw(user: String?, psw: String?) {
        loginInfo.edit().putString("user", user).apply()
        loginInfo.edit().putString("psw", psw).apply()
    }


    /**
     * 同步
     */

    suspend fun sync(): Boolean {
        try {
            return syncUsers() && syncNotes() && syncTodos() && syncTodos() && syncTasks()
        } catch (e: Exception) {
            Log.d("网络", "同步所有内容失败", e)
            return false
        }
    }

    suspend fun syncUsers(): Boolean {
        if (userRepository.syncAllUser())
            return true
        else {
            Log.d("网络", "同步所有用户失败")
            return false
        }
    }

    suspend fun syncNotes(): Boolean {
        if (!noteRepository.syncAllNotes(getUser()!!)) {
            Log.d("网络", "同步所有笔记失败")
            return false
        }
        // 更新本地状态
        _state.value.notes.run { clear();addAll(noteRepository.getAll(getUser()!!)) }
        return true // 如果所有操作成功，则返回 true
    }

    suspend fun syncTodos(): Boolean {
        if (!todoRepository.syncAllTodo(getUser()!!)) {
            Log.d("网络", "同步所有待办失败")
            return false
        }
        // 更新本地状态
        _state.value.todos.run { clear();addAll(todoRepository.getAll(getUser()!!)) }
        return true // 如果所有操作成功，则返回 true
    }

    suspend fun syncTasks(): Boolean {
        if (!taskRepository.syncAllTasks(getUser()!!) || !taskListRepository.syncTaskList(getUser()!!)) {
            Log.d("网络", "同步所有任务或任务表失败")
            return false
        }
        _state.value.tasks.run { clear();addAll(taskRepository.getAll(getUser()!!)) }
        _state.value.taskLists.run { clear();addAll(taskListRepository.getAll(getUser()!!)) }
        return true
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
            modifiedTime = intent.modifiedTime,
            user = getUser()!!
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
     * to do
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