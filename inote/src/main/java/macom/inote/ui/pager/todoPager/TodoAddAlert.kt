package macom.inote.ui.pager.todoPager

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import macom.inote.data.Todo
import macom.inote.ui.component.TaskAndTodoAlertDialog
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel
import java.util.Calendar

val todoIsAdd = mutableStateOf(false)


enum class RepeatType(val label: String, val value: Long) {
    None(label = "无", value = 0), Daily(label = "每天", value = 1), Weekly(
        label = "每周",
        value = 7
    ),
    Monthly(label = "每月", value = 30), Yearly(
        label = "每年", value = 365
    )
}

@Composable
fun TodoAddAlert(viewModel: INoteViewModel) {
    TaskAndTodoAlertDialog(
        viewModel = viewModel,
        isShowDialog = todoIsAdd,
        onConfirm = { task, todo, viewModel, nowTaskList, isShowDialog, text, remindDateTime, isSetReminder, repeatType, context ->
            addTodoOnConfirm(
                todoText = text,
                remindDateTime = remindDateTime,
                viewModel = viewModel,
                context = context,
                isSetReminder = isSetReminder,
                isShowDialog = isShowDialog,
                repeatType = repeatType
            )
        })
}

private fun addTodoOnConfirm(
    todoText: String,
    remindDateTime: Calendar,
    viewModel: INoteViewModel,
    context: Context,
    isSetReminder: Boolean,
    isShowDialog: MutableState<Boolean>,
    repeatType: RepeatType
) {
    if (todoText.isNotEmpty()) {
        val newTodo = Todo(
            isOver = false,
            body = todoText,
            createTime = System.currentTimeMillis(),
            remindTime = if (isSetReminder) remindDateTime.timeInMillis else null,
            user = viewModel.getUser()!!,
        )
        val intent = INoteIntent.AddToDO(newTodo)
        viewModel.addTodo(intent)
        if (isSetReminder) viewModel.scheduleNoteReminder(
            id = newTodo.user + newTodo.createTime,
            title = "iNote 待办",
            content = newTodo.body,
            delayInMillis = remindDateTime.timeInMillis - System.currentTimeMillis(),
            repeatPeriod = repeatType.value
        )
        Toast.makeText(
            context, "新建待办成功！", Toast.LENGTH_SHORT
        ).show()
        // 关闭Dialog
        isShowDialog.value = false
    } else {
        Toast.makeText(
            context, "待办内容为空！", Toast.LENGTH_SHORT
        ).show()
    }
}