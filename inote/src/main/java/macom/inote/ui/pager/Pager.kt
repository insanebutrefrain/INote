package macom.inote.ui.pager

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import macom.inote.R
import macom.inote.ui.pager.notePager.NoteFAB
import macom.inote.ui.pager.taskPager.TaskFAB
import macom.inote.ui.pager.todoPager.TodoFAB
import macom.inote.viewModel.INoteViewModel

/**
 * 屏幕实体类
 */
sealed class Pager(
    val route: String, //标识
    val title: String,
    val icon: Int,
    val screenLoader: @Composable (bottomPadding: PaddingValues, navController: NavHostController, viewModel: INoteViewModel) -> Unit,
    val FAB: @Composable (viewModel: INoteViewModel, navController: NavHostController) -> Unit
) {
    data object Note : Pager(route = "note",
        title = "笔记",
        icon = R.drawable.note,
        { padding, navController, viewModel ->
            NotePager(
                bottomPadding = padding, navController = navController, viewModel = viewModel
            )
        },
        FAB = { viewModel, navController -> NoteFAB(viewModel, navController) })

    data object Todo : Pager(route = "todo",
        title = "待办",
        icon = R.drawable.todo,
        screenLoader = { padding, navController, viewModel ->
            TodoPager(
                padding, navController, viewModel
            )
        },
        FAB = { viewModel, navController -> TodoFAB(viewModel, navController) })

    data object Task : Pager(route = "task",
        title = "任务",
        icon = R.drawable.task,
        screenLoader = { padding, navController, viewModel ->
            TaskPager(
                padding, navController, viewModel
            )
        },
        FAB = { viewModel, navController -> TaskFAB(viewModel, navController) })
}