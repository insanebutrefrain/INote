package macom.inote.ui.pager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.ui.component.AlertEdit
import macom.inote.ui.component.DeleteConfirmAlert
import macom.inote.ui.pager.taskPager.DrawerView
import macom.inote.ui.pager.taskPager.TaskAddAlert
import macom.inote.ui.pager.taskPager.TaskTopBar
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel

val isEditListName = mutableStateOf(false)
val isAddTaskList = mutableStateOf(false)
val isEditTask = mutableStateOf(false)
val selectEditTask = mutableStateOf(Task(false, "", System.currentTimeMillis(), null, 0))

/**
 * 任务页面
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun TaskPager(
    bottomPadding: PaddingValues, navController: NavHostController, viewModel: INoteViewModel
) {
    val state = viewModel.state.collectAsState()
    val tasks = state.value.tasks
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val allTaskLists = state.value.taskLists
    val nowTaskList = remember { mutableStateOf(allTaskLists[0]) }
    val context = LocalContext.current
    val isDeleteMode = mutableStateOf(false)
    val isShowDeleteAlert = mutableStateOf(false)
    val isDeleteMap = mutableStateMapOf<Task, Boolean>()
    tasks.forEach {
        isDeleteMap[it] = false
    }
    Scaffold(topBar = {
        TaskTopBar(
            isDeleteMode = isDeleteMode,
            isDeleteMap = isDeleteMap,
            isDeleteAlert = isShowDeleteAlert,
            viewModel = viewModel,
            nowTaskList = nowTaskList
        )
    }) { topPadding ->
        DeleteConfirmAlert(isShowDeleteAlert = isShowDeleteAlert, onDismiss = {
            isShowDeleteAlert.value = false
        }, onDeleteConfirmed = {
            isDeleteMap.forEach {
                if (it.value) {
                    val intent = INoteIntent.DeleteTask(it.key)
                    viewModel.deleteTask(intent)
                    isDeleteMap[it.key] = false
                }
            }
            if (nowTaskList.value.createTime != 0L) {
                var delete = true
                tasks.forEach { task ->
                    if (task.taskListId == nowTaskList.value.createTime) delete = false
                }
                if (delete) {
                    val intent = INoteIntent.DeleteTaskList(nowTaskList.value)
                    viewModel.deleteTaskList(intent)
                }
                nowTaskList.value = allTaskLists[0]
            }
            isDeleteMode.value = false
            isShowDeleteAlert.value = false
        }

        )
        AlertEdit(title = "修改任务",
            isShow = isEditTask,
            currentValue = selectEditTask.value.body,
            onDismiss = { isEditTask.value = false },
            onConfirm = { newBody ->
                val intent = INoteIntent.EditTask(
                    newBody = newBody,
                    task = selectEditTask.value,
                )
                viewModel.editTask(intent)
            })
        TaskAddAlert(viewModel = viewModel, nowTaskList = nowTaskList)
        AlertEdit(title = "添加任务表",
            isShow = isAddTaskList,
            currentValue = "",
            onDismiss = { isAddTaskList.value = false },
            onConfirm = { listName ->
                val newTaskList = TaskList(
                    listName, System.currentTimeMillis()
                )
                val intent = INoteIntent.AddTaskList(newTaskList)
                viewModel.addTaskList(intent)
                nowTaskList.value = newTaskList
            })
        AlertEdit(title = "修改列表名",
            isShow = isEditListName,
            currentValue = nowTaskList.value.listName,
            onDismiss = { isEditListName.value = false },
            onConfirm = { listName ->
                val intent = INoteIntent.EditTaskListName(
                    listName = listName, nowTaskList = nowTaskList.value
                )
                viewModel.editTaskListName(intent)
                nowTaskList.value = nowTaskList.value.copy(listName = listName)
            })
        DrawerView(
            bottomPadding = bottomPadding,
            topPadding = topPadding,
            drawerState = drawerState,
            nowTaskList = nowTaskList,
            scope = scope,
            viewModel = viewModel,
            isDeleteMap = isDeleteMap,
            isDeleteMode = isDeleteMode
        )
    }
}