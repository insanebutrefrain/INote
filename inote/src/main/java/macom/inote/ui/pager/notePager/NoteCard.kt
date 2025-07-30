package macom.inote.ui.pager.notePager

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import macom.inote.data.Note
import macom.inote.ui.helper.formatTimestamp

/**
 * 单个笔记卡片
 */

@Composable
fun NoteCard(
    note: Note,
    navController: NavHostController,
    isDeleteMode: MutableState<Boolean>,
    isDeleteMap: SnapshotStateMap<Note, Boolean>
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // 长按事件的处理
                isDeleteMode.value = true  // 进入删除模式
                isDeleteMap[note] = true   // 选中当前笔记
            }, onTap = {
                val noteStr = Gson().toJson(note)
                navController.navigate("editNote/${noteStr}") {}
            })
        }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, // 子控件均匀分布
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth() // 使得Row宽度填满父容器
        ) {
            // Column占用剩余空间
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .weight(1f) // Column占据剩余空间
            ) {
                Text(
                    text = note.title,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = note.body,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatTimestamp(note.modifiedTime),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            // Checkbox在最右端
            if (isDeleteMode.value) {
                Checkbox(checked = isDeleteMap[note] ?: false, // 使用 null 安全操作符
                    onCheckedChange = { isChecked ->
                        isDeleteMap[note] = isChecked
                    })
            }
        }
    }
}