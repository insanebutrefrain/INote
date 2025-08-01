package macom.inote.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import macom.inote.ui.navigation.MainNavigation
import macom.inote.ui.theme.INotesTheme
import macom.inote.viewModel.INoteViewModel


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: INoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this).get(INoteViewModel::class.java)
        // 检查通知权限是否已经授予 注意：API 33以上版本需要检查POST_NOTIFICATIONS发布通知权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        setContent {
            INotesTheme {
                MainNavigation(viewModel)
            }
        }
    }
}







