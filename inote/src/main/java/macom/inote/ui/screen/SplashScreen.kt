package macom.inote.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import macom.inote.R
import macom.inote.viewModel.INoteViewModel

/**
 * 用于判断是否登录来确定首个界面的闪屏，已经弃用
 */
@Composable
fun SplashScreen(navController: NavHostController, viewModel: INoteViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        // 延迟一下，模拟闪屏
        delay(timeMillis = 500)

        // 根据登录状态导航
        if (viewModel.getUser() != null && viewModel.getPsw() != null) {
            Toast.makeText(context, viewModel.getPsw().toString(), Toast.LENGTH_SHORT).show()
            navController.navigate(route = "mainPager")
        } else {
            navController.navigate(route = "login")
        }

    }
    SplashUI()
}

@Preview
@Composable
fun SplashUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(R.mipmap.icon), contentDescription = "icon")
        Text(
            text = "iNote",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

