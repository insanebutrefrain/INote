package macom.inote.viewModel

import macom.inote.data.Note
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.data.Todo
import macom.inote.data.User

sealed class INoteIntent {
    // 用户
    data class Register(val user: User)
    data class Login(val id: String, val psw: String)
    data object Logout

    // 笔记
    data class AddNote(val note: Note)
    data class EditNote(
        val title: String,
        val body: String,
        val modifiedTime: Long,
        val createTime: Long
    )

    data class DeleteNote(val note: Note)

    // 待办
    data class AddToDO(val todo: Todo)
    data class CheckTodo(val todo: Todo)
    data class EditTodo(val newBody: String, val todo: Todo)
    data class DeleteTodo(val todo: Todo)

    // 任务表
    data class AddTaskList(val taskList: TaskList)
    data class EditTaskListName(val listName: String, val nowTaskList: TaskList)
    data class DeleteTaskList(val taskList: TaskList)

    // 任务
    data class AddTask(val newTask: Task, val nowTaskList: TaskList)
    data class CheckTask(val task: Task)
    data class EditTask(val newBody: String, val task: Task)
    data class DeleteTask(val task: Task)
}