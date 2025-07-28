package macom.inote.viewModel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import macom.inote.data.Note
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.data.Todo

data class INoteState(
    var notes: SnapshotStateList<Note> = mutableStateListOf(),
    var todos: SnapshotStateList<Todo> = mutableStateListOf(),
    var taskLists: SnapshotStateList<TaskList> = mutableStateListOf(TaskList(listName = "全部", createTime = 0)),
    var tasks:SnapshotStateList<Task> = mutableStateListOf()
)
