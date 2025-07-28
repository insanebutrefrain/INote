package macom.inote.ui.pager.taskPager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.ui.pager.todoPager.RepeatType
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel
import java.util.Calendar

val taskIsAdd = mutableStateOf(false)


/**
 * 添加任务界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskAddAlertDialog(
    viewModel: INoteViewModel,
    nowTaskList: MutableState<TaskList>,
    showDialog: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
    var todoText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedTime by remember { mutableStateOf(Calendar.getInstance()) }
    var setReminder by remember { mutableStateOf(false) }
    var repeatType by remember { mutableStateOf(RepeatType.None) } // 新增的重复类型
    var expanded by remember { mutableStateOf(false) } // 控制重复选项的展开

    // 控制键盘的弹出与隐藏
    val context = LocalContext.current

    // 控制日期和时间选择器的弹出
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var showTimePickerDialog by remember { mutableStateOf(false) }


    if (showDialog.value) {
        todoText = ""
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus() // 请求焦点
            keyboardController?.show() // 显示软键盘
        }
        Dialog(onDismissRequest = onDismiss) {
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
                            value = todoText,
                            onValueChange = { todoText = it },
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
                                    if (todoText.isNotEmpty()) {
                                        val newTask = Task(
                                            isOver = false,
                                            body = todoText,
                                            createTime = System.currentTimeMillis(),
                                            remindTime = selectedTime.timeInMillis,
                                            taskListId = nowTaskList.value.createTime
                                        )
                                        val intent = INoteIntent.AddTask(
                                            newTask = newTask, nowTaskList = nowTaskList.value
                                        )
                                        viewModel.addTask(intent)
                                        Toast.makeText(
                                            context, "新建任务成功", Toast.LENGTH_SHORT
                                        ).show()
                                        onDismiss()  // 关闭Dialog
                                    } else {
                                        Toast.makeText(
                                            context, "任务内容为空！", Toast.LENGTH_SHORT
                                        ).show()
                                    }
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
                            FilterChip(selected = setReminder, onClick = {}, label = {// 开关
                                Text("提醒")
                            }, trailingIcon = {
                                Switch(
                                    checked = setReminder,
                                    onCheckedChange = { setReminder = it },
                                    modifier = Modifier.padding()
                                )
                            })

                            // 选择重复选项，通过 ExposedDropdownMenuBox 展开
                            ExposedDropdownMenuBox(
                                expanded = expanded,
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
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }) {
                                    RepeatType.values().forEach { type ->
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
                        if (setReminder) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                // 选择日期按钮
                                TextButton(onClick = { showDatePickerDialog = true }) {
                                    Text(
                                        text = "${selectedDate.get(Calendar.MONTH) + 1}/${
                                            selectedDate.get(
                                                Calendar.DAY_OF_MONTH
                                            )
                                        }/${selectedDate.get(Calendar.YEAR)}", fontSize = 18.sp
                                    )
                                }
                                TextButton(onClick = { showTimePickerDialog = true }) {
                                    Text(
                                        text = "${selectedTime.get(Calendar.HOUR_OF_DAY)}:${
                                            selectedTime.get(
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
                selectedDate = calendar
                showDatePickerDialog = false
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
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
                    selectedTime.get(Calendar.YEAR),
                    selectedTime.get(Calendar.MONTH),
                    selectedTime.get(Calendar.DAY_OF_MONTH),
                    hourOfDay,
                    minute
                )
                selectedTime = calendar
                showTimePickerDialog = false
            }, selectedTime.get(Calendar.HOUR_OF_DAY), selectedTime.get(Calendar.MINUTE), true
        ).apply {
            setOnDismissListener {
                // 在对话框关闭时，状态不会被重置
                showTimePickerDialog = false
            }
        }.show()
    }
}

