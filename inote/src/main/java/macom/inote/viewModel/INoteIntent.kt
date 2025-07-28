package macom.inote.viewModel

import macom.inote.data.Note
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.data.Todo

sealed class INoteIntent {
    data class AddNote(val note: Note)
    data class EditNote(
        val title: String,
        val body: String,
        val modifiedTime: Long,
        val createTime: Long
    )

    data class DeleteNote(val note: Note)

    data class AddToDO(val todo: Todo)
    data class CheckTodo(val todo: Todo)
    data class EditTodo(val newBody: String, val todo: Todo)
    data class DeleteTodo(val todo: Todo)

    data class AddTaskList(val taskList: TaskList)
    data class EditTaskListName(val listName: String, val nowTaskList: TaskList)
    data class DeleteTaskList(val taskList:TaskList)

    data class AddTask(val newTask: Task, val nowTaskList: TaskList)
    data class CheckTask(val task: Task)
    data class EditTask(val newBody: String, val task: Task)
    data class DeleteTask(val task: Task)
}