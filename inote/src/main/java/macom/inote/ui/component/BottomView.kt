package macom.inote.ui.component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import macom.inote.R
import macom.inote.ui.screen.pagers

/**
 * 绘制底部导航栏
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BottomView(scope: CoroutineScope, pagerState: PagerState) {
    BottomAppBar(modifier = Modifier.height(70.dp)) {
        pagers.forEach { it ->
            NavigationBarItem(selected = false, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(
                        pagers.indexOf(it),
                        animationSpec = tween(durationMillis = 800) // 动画持续时间
                    )
                }

            }, icon = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = ImageVector.vectorResource(
                            id = it.icon
                        ),
                        contentDescription = it.title,
                        tint = if (pagerState.currentPage == pagers.indexOf(it)) colorResource(R.color.myBlue) else Color.Unspecified
                    )
                    Text(text = it.title, fontSize = 10.sp, fontFamily = FontFamily.Serif)
                }
            })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun BottomViewPreview() {
    BottomView(
        scope = rememberCoroutineScope(),
        pagerState = rememberPagerState(initialPage = 0, pageCount = { pagers.size })
    )
}
