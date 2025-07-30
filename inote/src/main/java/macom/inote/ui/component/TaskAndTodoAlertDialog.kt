package macom.inote.ui.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.data.Todo
import macom.inote.ui.pager.todoPager.RepeatType
import macom.inote.viewModel.INoteViewModel
import java.util.Calendar


/**
 * 任务或代办的添加弹窗界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAndTodoAlertDialog(
    task: Task? = null,
    todo: Todo? = null,
    viewModel: INoteViewModel,
    nowTaskList: MutableState<TaskList>? = null,
    isShowDialog: MutableState<Boolean>,
    onConfirm: (
        task: Task?,
        todo: Todo?,
        viewModel: INoteViewModel,
        nowTaskList: MutableState<TaskList>?,
        isShowDialog: MutableState<Boolean>,
        text: String,
        remindDateTime: Calendar,
        isSetReminder: Boolean,
        repeatType: RepeatType,
        context: Context
    ) -> Unit
) {
    var text by remember { mutableStateOf(value = "") }
    var remindDateTime by remember { mutableStateOf(Calendar.getInstance()) }
    // 是否提醒
    var isSetReminder by remember { mutableStateOf(value = false) }
    // 重复类型
    var repeatType by remember { mutableStateOf(RepeatType.None) } // 新增的重复类型
    // 控制重复选项的展开
    var expanded by remember { mutableStateOf(value = false) }
    // 控制键盘的弹出与隐藏
    val context = LocalContext.current
    // 控制日期和时间选择器的弹出
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }

    if (isShowDialog.value) {
        text = todo?.body ?: task?.body ?: ""
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus() // 请求焦点
            keyboardController?.show() // 显示软键盘
        }
        Dialog(onDismissRequest = { isShowDialog.value = false }) {
            Box(
                modifier = Modifier
            ) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 16.dp,
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .imePadding() // 防止键盘遮挡
                            .padding(10.dp)
                    ) {

                        // 待办事项内容输入框
                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            label = { Text("输入任务") },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = {
                                keyboardController?.hide()
                            }),
                            leadingIcon = {
                                Checkbox(checked = false, onCheckedChange = {})
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    onConfirm(
                                        task,
                                        todo,
                                        viewModel,
                                        nowTaskList,
                                        isShowDialog,
                                        text,
                                        remindDateTime,
                                        isSetReminder,
                                        repeatType,
                                        context
                                    )
                                }) { Icon(Icons.Filled.Check, contentDescription = null) }
                            },
                            singleLine = true,
                            textStyle = TextStyle(fontWeight = FontWeight.Normal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester)
                        )


                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 5.dp)  // 添加一些水平内边距，使布局不至于贴边
                        ) {
                            FilterChip(selected = isSetReminder, onClick = {}, label = {// 开关
                                Text(text = "提醒")
                            }, trailingIcon = {
                                Switch(
                                    checked = isSetReminder,
                                    onCheckedChange = { isSetReminder = it },
                                )
                            })

                            // 选择重复选项，通过 ExposedDropdownMenuBox 展开
                            ExposedDropdownMenuBox(expanded = expanded,
                                onExpandedChange = { expanded = it }) {
                                OutlinedTextField(
                                    value = repeatType.label,
                                    onValueChange = {},
                                    label = { Text("重复") },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Expand"
                                        )
                                    },
                                    modifier = Modifier.menuAnchor()
                                )
                                ExposedDropdownMenu(expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    RepeatType.entries.forEach { type ->
                                        DropdownMenuItem(text = { Text(text = type.label) },
                                            onClick = {
                                                repeatType = type
                                                expanded = false
                                            })
                                    }
                                }
                            }
                        }

                        // 如果开启了提醒，显示日期和时间选择
                        if (isSetReminder) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // 选择日期按钮
                                TextButton(onClick = { showDatePickerDialog = true }) {
                                    Text(
                                        text = "${remindDateTime.get(Calendar.MONTH) + 1}/${
                                            remindDateTime.get(
                                                Calendar.DAY_OF_MONTH
                                            )
                                        }/${remindDateTime.get(Calendar.YEAR)}", fontSize = 18.sp
                                    )
                                }
                                TextButton(onClick = { showTimePickerDialog = true }) {
                                    Text(
                                        text = "${remindDateTime.get(Calendar.HOUR_OF_DAY)}:${
                                            remindDateTime.get(
                                                Calendar.MINUTE
                                            )
                                        }", fontSize = 20.sp
                                    )
                                }
                            }
                        }


                    }
                }
            }
        }
    }

    // 日期选择器Dialog
    if (showDatePickerDialog) {
        DatePickerDialog(
            LocalContext.current,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, dayOfMonth)
                remindDateTime = calendar
                showDatePickerDialog = false
            },
            remindDateTime.get(Calendar.YEAR),
            remindDateTime.get(Calendar.MONTH),
            remindDateTime.get(Calendar.DAY_OF_MONTH)
        ).apply {
            setOnDismissListener {
                showDatePickerDialog = false
            }
        }.show()
    }

    // 时间选择器Dialog
    if (showTimePickerDialog) {
        TimePickerDialog(
            LocalContext.current, { _, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(
                    remindDateTime.get(Calendar.YEAR),
                    remindDateTime.get(Calendar.MONTH),
                    remindDateTime.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute
                )
                remindDateTime = calendar
                showTimePickerDialog = false
            }, remindDateTime.get(Calendar.HOUR_OF_DAY), remindDateTime.get(Calendar.MINUTE), true
        ).apply {
            setOnDismissListener {
                // 在对话框关闭时，状态不会被重置
                showTimePickerDialog = false
            }
        }.show()
    }
}


@Preview
@Composable
fun AddAlertDialogPreview() {

}