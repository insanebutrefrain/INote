package macom.inote.ui.pager.notePager

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import macom.inote.viewModel.INoteViewModel

// 按照创建时间排序、按照修改时间排序
enum class NoteOrder {
    ByCreateTime, ByModifiedTime
}
/**
 * 下拉菜单
 */
@Composable
fun NoteMenuView(
    isExpand: MutableState<Boolean>,
    isDeleteMode: MutableState<Boolean>,
    noteOrder: MutableState<NoteOrder>,
    viewModel: INoteViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    DropdownMenu(expanded = isExpand.value, onDismissRequest = {
        isExpand.value = false
    }) {
        DropdownMenuItem(text = { Text("编辑") }, onClick = {
            isDeleteMode.value = true
            isExpand.value = false
        })
        DropdownMenuItem(text = { Text("同步") }, onClick = {
            scope.launch {
                if (viewModel.syncNotes()) {
                    Toast.makeText(context, "笔记同步成功！", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "笔记同步失败，请检查网络！", Toast.LENGTH_SHORT).show()
                }
            }
            isExpand.value = false
        })
        Spacer(
            Modifier
                .height(5.dp)
                .background(Color.Black)
        )
        DropdownMenuItem(text = { Text("按修改时间排序") }, onClick = {
            noteOrder.value = NoteOrder.ByModifiedTime
            isExpand.value = false
        }, trailingIcon = {
            if (noteOrder.value == NoteOrder.ByModifiedTime) Icon(
                Icons.Filled.Check,
                contentDescription = null
            )
        })
        DropdownMenuItem(text = { Text("按创建时间排序") }, onClick = {
            noteOrder.value = NoteOrder.ByCreateTime
            isExpand.value = false
        }, trailingIcon = {
            if (noteOrder.value == NoteOrder.ByCreateTime) Icon(
                Icons.Filled.Check,
                contentDescription = null
            )
        })
    }
}