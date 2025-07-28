package macom.inote.ui.pager.todoPager

import androidx.compose.runtime.Composable
import macom.inote.viewModel.INoteViewModel

@Composable
fun TodoAddAlert(viewModel: INoteViewModel) {
    if (todoIsAdd.value) {
        ToDoAlertDialog(viewModel = viewModel, todoIsAdd, onDismiss = { todoIsAdd.value = false })
    }
}
