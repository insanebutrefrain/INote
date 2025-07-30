package macom.inote.ui.pager.todoPager

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.navigation.NavHostController
import macom.inote.data.Todo
import macom.inote.ui.pager.Pager
import macom.inote.viewModel.INoteViewModel

/**
 * 待办页面 顶部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoTopBar(
    isDeleteMode: MutableState<Boolean>,
    isDeleteMap: SnapshotStateMap<Todo, Boolean>,
    isDeleteAlert: MutableState<Boolean>,
    viewModel: INoteViewModel,
    navController: NavHostController
) {
    val isExpand = remember { mutableStateOf(false) }
    if (!isDeleteMode.value) TopAppBar(title = { Text(Pager.Todo.title) },
        // 导航图标
//        navigationIcon = {
//            Icon(painterResource(R.drawable.todo_outlined), contentDescription = null)
//        },
        actions = {
            IconButton(onClick = {
                isExpand.value = true
            }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "更多")
                TodoMenuView(
                    isExpand = isExpand,
                    isDeleteMode = isDeleteMode,
                    viewModel = viewModel,
                    navController = navController
                )
            }
        })
    else {
        TopAppBar(
            title = {
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
                    isDeleteMap.forEach {
                        isDeleteMap[it.key] = true
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