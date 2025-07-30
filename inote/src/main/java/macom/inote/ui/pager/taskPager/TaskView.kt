package macom.inote.ui.pager.taskPager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import macom.inote.data.Task
import macom.inote.data.TaskList
import macom.inote.ui.pager.isAddTaskList
import macom.inote.ui.pager.isEditListName
import macom.inote.viewModel.INoteViewModel

/**
 * 主界面
 */
@Composable
fun TaskView(
    scope: CoroutineScope,
    drawerState: DrawerState,
    nowTaskList: MutableState<TaskList>,
    viewModel: INoteViewModel,
    isDeleteMap: SnapshotStateMap<Task, Boolean>,
    isDeleteMode: MutableState<Boolean>,
    bottomPadding: PaddingValues,
    topPadding: PaddingValues
) {
    val state = viewModel.state.collectAsState()
    val isExpanded = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(
            top = topPadding.calculateTopPadding(),
            bottom = bottomPadding.calculateBottomPadding()
        ),
        verticalArrangement = Arrangement.Top,
    ) {
        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                IconButton(onClick = {
                    isAddTaskList.value = true
                }) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "添加任务列表"
                    )
                }
            }
            items(state.value.taskLists) { taskList ->
                FilterChip(selected = nowTaskList.value == taskList,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    onClick = {
                        nowTaskList.value = taskList
                    },
                    label = {
                        Text(
                            modifier = Modifier,
                            text = taskList.listName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    trailingIcon = {
                        if (nowTaskList.value == taskList) Icon(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { isEditListName.value = true },
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "修改名字"
                        )

                    })
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //还未完成的待办
            items(state.value.tasks) { task ->
                // 用 `AnimatedVisibility` 来实现动画
                AnimatedVisibility(
                    visible = !task.isOver && (task.taskListId == nowTaskList.value.createTime || (nowTaskList.value.createTime == 0L)),
                    enter = scaleIn(
                        initialScale = 0f, animationSpec = tween(
                            durationMillis = 300, easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        targetAlpha = 0f, animationSpec = tween(
                            durationMillis = 300, easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    TaskCard(
                        task = task,
                        isDeleteMode = isDeleteMode,
                        isDeleteMap = isDeleteMap,
                        viewModel = viewModel
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    IconButton(onClick = {
                        isExpanded.value = !isExpanded.value
                    }) {
                        Icon(
                            imageVector = if (isExpanded.value) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowRight,
                            contentDescription = null
                        )
                    }
                    Text("已完成")
                }
            }
            items(state.value.tasks) { task ->
                // 用 `AnimatedVisibility` 来实现动画
                AnimatedVisibility(
                    visible = isExpanded.value && task.isOver && (task.taskListId == nowTaskList.value.createTime || (nowTaskList.value.createTime == 0L)),
                    enter = scaleIn(
                        initialScale = 0f, animationSpec = tween(
                            durationMillis = 300, easing = FastOutSlowInEasing
                        )
                    ),
                    exit = fadeOut(
                        targetAlpha = 0f, animationSpec = tween(
                            durationMillis = 300, easing = FastOutSlowInEasing
                        )
                    )
                ) {
                    TaskCard(
                        task = task,
                        isDeleteMode = isDeleteMode,
                        isDeleteMap = isDeleteMap,
                        viewModel = viewModel
                    )
                }
            }
        }

    }

}