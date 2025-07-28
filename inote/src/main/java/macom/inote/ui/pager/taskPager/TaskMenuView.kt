package macom.inote.ui.pager.taskPager

import android.widget.Toast
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import macom.inote.viewModel.INoteViewModel

@Composable
fun TaskMenuView(
    isExpand: MutableState<Boolean>,
    isDeleteMode: MutableState<Boolean>,
    viewModel: INoteViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    DropdownMenu(expanded = isExpand.value, onDismissRequest = {
        isExpand.value = false
    }) {

        DropdownMenuItem(text = { Text("编辑") }, onClick = {
            isDeleteMode.value = true
            isExpand.value = false
        })
        DropdownMenuItem(text = { Text("同步") }, onClick = {
            scope.launch {
                if (viewModel.syncTasks()) {
                    Toast.makeText(context, "任务同步成功！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "任务同步失败，请检查网络！", Toast.LENGTH_SHORT).show()
                }
            }
            isExpand.value = false
        })
    }
}