package macom.inote.ui.pager.taskPager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.ui.pager.Pager
import macom.inote.viewModel.INoteViewModel

/**
 * 任务界面 顶部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTopBar(
    isDeleteMode: MutableState<Boolean>,
    isDeleteMap: SnapshotStateMap<Task, Boolean>,
    isDeleteAlert: MutableState<Boolean>,
    viewModel: INoteViewModel,
    nowTaskList: MutableState<TaskList>
) {
    val isExpand = remember { mutableStateOf(false) }
    // 正常模式下的顶部导航栏
    if (!isDeleteMode.value)
        TopAppBar(
            title = { Text(Pager.Task.title) },
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            // 导航图标
            navigationIcon = {
//            Icon(painterResource(R.drawable.task_outlined), contentDescription = null)
            }, actions = {
                IconButton(onClick = {
                    isExpand.value = true
                }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "更多")
                    TaskMenuView(isExpand, isDeleteMode, viewModel = viewModel)
                }
            })
    else {
        TopAppBar(title = {
            TextButton(onClick = {
                isDeleteMode.value = false
                isDeleteMap.forEach {
                    isDeleteMap[it.key] = false
                }
                isExpand.value = false
            }) {
                Text(text = "取消")
            }
        },
            // 导航图标
            navigationIcon = {

            }, actions = {
                TextButton(onClick = {
                    if (nowTaskList.value.createTime == 0L) {
                        isDeleteMap.forEach {
                            isDeleteMap[it.key] =
                                true
                        }
                    } else {
                        isDeleteMap.forEach {
                            if (it.key.taskListId == nowTaskList.value.createTime) isDeleteMap[it.key] =
                                true
                        }
                    }
                    isExpand.value = false
                }) {
                    Text(text = "全选")
                }
                TextButton(onClick = {
                    isDeleteAlert.value = true
                    isExpand.value = false
                }) {
                    Text("删除")
                }
            })
    }
}