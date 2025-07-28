package macom.inote.ui.pager.taskPager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import macom.inote.data.TaskList
import macom.inote.viewModel.INoteViewModel

@Composable
fun TaskAddAlert(viewModel: INoteViewModel, nowTaskList: MutableState<TaskList>) {
    TaskAddAlertDialog(viewModel = viewModel,
        nowTaskList = nowTaskList,
        showDialog = taskIsAdd,
        onDismiss = { taskIsAdd.value = false })
}