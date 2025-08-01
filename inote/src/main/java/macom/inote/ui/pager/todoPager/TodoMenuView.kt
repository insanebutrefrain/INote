package macom.inote.ui.pager.todoPager

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import macom.inote.viewModel.INoteViewModel

/**
 * 下拉菜单
 */
@Composable
fun TodoMenuView(
    isExpand: MutableState<Boolean>, isDeleteMode: MutableState<Boolean>, viewModel: INoteViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    DropdownMenu(expanded = isExpand.value, onDismissRequest = {
        isExpand.value = false
    }) {

        DropdownMenuItem(leadingIcon = {
            Icon(imageVector = Icons.Filled.Edit, contentDescription = "编辑")
        }, text = { Text(text = "编辑") }, onClick = {
            isDeleteMode.value = true
            isExpand.value = false
        })
        DropdownMenuItem(leadingIcon = {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "同步")
        }, text = { Text("同步") }, onClick = {
            scope.launch {
                if (viewModel.sync()) {
                    Toast.makeText(context, "同步成功！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "同步失败，请检查网络！", Toast.LENGTH_SHORT).show()
                }
            }
            isExpand.value = false
        })
        DropdownMenuItem(leadingIcon = {
            Icon(imageVector = Icons.Filled.AccountBox, contentDescription = "我的")
        }, text = { Text("我的") }, onClick = {
            navController.navigate(route = "profile")
        })
    }
}