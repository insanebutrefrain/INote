package macom.inote.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import macom.inote.ui.screen.pagers
import macom.inote.viewModel.INoteViewModel

/**
 * 添加按钮
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FAB(
    pagerState: PagerState,
    viewModel: INoteViewModel,
    navController: NavHostController
) {
    pagers.forEach {
        AnimatedVisibility(
            visible = pagers.indexOf(it) == pagerState.currentPage,
            enter = fadeIn(
                initialAlpha = 0f,   // 开始时完全透明
                animationSpec = tween(
                    durationMillis = 300,   // 动画持续时间，单位毫秒
                    easing = FastOutSlowInEasing  // 采用平滑的缓动函数
                )
            ) + scaleIn(
                initialScale = 0f,
                animationSpec = tween(delayMillis = 300, easing = FastOutSlowInEasing)
            ),
            exit = scaleOut(
                targetScale = 0f,
                animationSpec = tween(delayMillis = 300, easing = FastOutSlowInEasing)
            ) + fadeOut(
                targetAlpha = 0f,  // 结束时完全透明
                animationSpec = tween(
                    durationMillis = 300,   // 动画持续时间，800毫秒
                    easing = EaseInOut    // 采用平滑的缓动函数
                )
            )
        ) {
            it.FAB(viewModel, navController)
        }
    }
}