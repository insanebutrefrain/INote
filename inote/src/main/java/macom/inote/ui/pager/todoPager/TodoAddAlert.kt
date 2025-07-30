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


enum class RepeatType(val label: String) {
    None(label = "无"), Daily(label = "每天"), Weekly(label = "每周"), Monthly(label = "每月"), Yearly(
        label = "每年"
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
                remindDataTime = remindDateTime,
                viewModel = viewModel,
                context = context,
                isSetReminder = isSetReminder,
                isShowDialog = isShowDialog
            )
        })
}

private fun addTodoOnConfirm(
    todoText: String,
    remindDataTime: Calendar,
    viewModel: INoteViewModel,
    context: Context,
    isSetReminder: Boolean,
    isShowDialog: MutableState<Boolean>
) {
    if (todoText.isNotEmpty()) {
        val newTodo = Todo(
            isOver = false,
            body = todoText,
            createTime = System.currentTimeMillis(),
            remindTime = if (isSetReminder) remindDataTime.timeInMillis else null,
            user = "123", // todo user
        )
        val intent = INoteIntent.AddToDO(newTodo)
        viewModel.addTodo(intent)
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