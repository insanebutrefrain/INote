package macom.inote.ui.pager.taskPager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.viewModel.INoteViewModel

/**
 * 抽屉界面
 */
@Composable
fun DrawerView(
    bottomPadding: PaddingValues,
    topPadding: PaddingValues,
    drawerState: DrawerState,
    nowTaskList: MutableState<TaskList>,
    scope: CoroutineScope,
    viewModel: INoteViewModel,
    isDeleteMap: SnapshotStateMap<Task, Boolean>,
    isDeleteMode: MutableState<Boolean>,
) {
    val allTaskLists = viewModel.state.collectAsState().value.taskLists
    ModalNavigationDrawer(modifier = Modifier.padding(
        bottom = bottomPadding.calculateBottomPadding(), top = topPadding.calculateTopPadding()
    ), drawerState = drawerState, gesturesEnabled = true, drawerContent = {
        ModalDrawerSheet {
            LazyColumn(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .fillMaxHeight()
                    .fillMaxWidth(0.7f)
            ) {
                items(allTaskLists) {
                    NavigationDrawerItem(shape = RectangleShape,
                        selected = it == nowTaskList.value,
                        label = {
                            Text(text = it.listName, fontSize = 25.sp)
                        },
                        icon = {
                            Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                        },
                        onClick = {
                            scope.launch {
                                nowTaskList.value = it
                                drawerState.close()
                            }
                        })
                }
            }
        }
    }) {
        // 在这里调用
        TaskView(
            scope = scope,
            drawerState = drawerState,
            nowTaskList = nowTaskList,
            viewModel = viewModel,
            isDeleteMap = isDeleteMap,
            isDeleteMode = isDeleteMode
        )
    }
}