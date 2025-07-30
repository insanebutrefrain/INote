package macom.inote.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import macom.inote.R
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: INoteViewModel
) {
    val isLogoutAlert = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 20.dp, end = 20.dp, top = 50.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 应用图标和名称
        AppInfoSection()

        Spacer(modifier = Modifier.height(32.dp))

        // 用户头像和信息
        UserProfileSection(viewModel = viewModel)

        Spacer(modifier = Modifier.height(32.dp))

        // 账号信息
        AccountInfoSection(viewModel = viewModel)

        Spacer(modifier = Modifier.weight(1f))

        // 退出登录按钮
        LogoutButton(isLogoutAlert)

        LogoutConfirmAlert(
            isLogoutAlert = isLogoutAlert,
            onLogoutConfirmed = {
                isLogoutAlert.value = false
                viewModel.logout(INoteIntent.Logout)
                navController.navigate(route = "login")
            }
        )
    }
}

@Composable
private fun AppInfoSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.mipmap.icon),
            contentDescription = "App Icon",
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "iNote",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun UserProfileSection(viewModel: INoteViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.me),
            contentDescription = "用户头像r",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column {
            Text(
                text = viewModel.state.value.user.value?.name ?: "用户为空！",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = viewModel.state.value.user.value?.signs ?: "这个人很懒，什么也没留下！",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun AccountInfoSection(viewModel: INoteViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
    ) {
        InfoRow(title = "账号", value = viewModel.state.value.user.value?.id ?: "账号为空")
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
        val calendar = Calendar.getInstance().apply {
            timeInMillis = viewModel.state.value.user.value?.registerTime ?: 0
        }
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        InfoRow(
            title = "注册时间",
            value = dateFormat.format(calendar.time)

        )
    }
}

@Composable
private fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun LogoutButton(isLogoutAlert: MutableState<Boolean>) {
    Button(
        onClick = {
            isLogoutAlert.value = true
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text = "退出登录", fontSize = 16.sp)
    }
}

@Composable
fun LogoutConfirmAlert(
    isLogoutAlert: MutableState<Boolean>,
    onLogoutConfirmed: () -> Unit
) {
    if (isLogoutAlert.value) {
        AlertDialog(
            onDismissRequest = { isLogoutAlert.value = false },
            title = { Text(text = "确认退出") },
            text = { Text(text = "您确定要退出登录吗?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onLogoutConfirmed() // 确认删除
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isLogoutAlert.value = false
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}