package macom.inote.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import macom.inote.data.Note
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.data.Todo
import macom.inote.data.User

data class INoteState(
    var notes: SnapshotStateList<Note> = mutableStateListOf(),
    var todos: SnapshotStateList<Todo> = mutableStateListOf(),
    var taskLists: SnapshotStateList<TaskList> = mutableStateListOf(),
    var tasks: SnapshotStateList<Task> = mutableStateListOf(),
    val user: MutableState<User?> = mutableStateOf(value = null)
)
