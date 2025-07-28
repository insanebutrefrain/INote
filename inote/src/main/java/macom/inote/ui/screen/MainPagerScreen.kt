package macom.inote.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import macom.inote.ui.component.BottomView
import macom.inote.ui.component.FAB
import macom.inote.ui.pager.Pager
import macom.inote.viewModel.INoteViewModel

/**
 * 屏幕列表
 */
val pagers = listOf(Pager.Task, Pager.Note, Pager.Todo)

/**
 * 滑动 pager界面
 */
@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPagerScreen(viewModel: INoteViewModel, navController: NavHostController) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pagers.size },
    )
    val scope = rememberCoroutineScope()
    Scaffold(bottomBar = {
        // 底部导航栏
        BottomView(scope = scope, pagerState = pagerState)
    }, floatingActionButton = {
        FAB(pagerState, viewModel, navController)
    }) { innerPadding ->
        HorizontalPager(pagerState) { page: Int ->
            pagers[page].screenLoader(innerPadding, navController, viewModel)
        }
    }
}



