package macom.inote.ui.pager.todoPager

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import macom.inote.data.Todo
import macom.inote.ui.pager.isEditTodo
import macom.inote.ui.pager.selectedTodo
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel

/**
 * 单张卡片
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun TodoCard(
    todo: Todo,
    viewModel: INoteViewModel,
    isDeleteMode: MutableState<Boolean>,
    isDeleteMap: SnapshotStateMap<Todo, Boolean>
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .alpha(if (todo.isOver) 0.6f else 1.0f)
        .padding(vertical = 10.dp, horizontal = 20.dp)
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // 长按事件的处理
                isDeleteMode.value = true  // 进入删除模式
                isDeleteMap[todo] = true   // 选中当前笔记
            })
        }
        // 动态调整大小
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .animateContentSize(), // 动态变化的动画
            horizontalArrangement = Arrangement.SpaceBetween, // 保证左右元素分布
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.animateContentSize(), // 动态变化的动画
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 让 Checkbox 在最左边
                Checkbox(modifier = Modifier.alpha(if (isDeleteMode.value) 0f else 1.0f),
                    checked = todo.isOver,
                    onCheckedChange = {
                        val intent = INoteIntent.CheckTodo(todo)
                        viewModel.checkTodo(intent)
                    })
                // Text 占据剩余空间
                Text(text = todo.body, fontSize = 20.sp, modifier = Modifier.clickable {
                    isEditTodo.value = true
                    selectedTodo.value = todo
                })
            }
            // 让 Checkbox 在最右边，当处于删除模式时才显示
            if (isDeleteMode.value) {
                Checkbox(checked = isDeleteMap[todo] ?: false, onCheckedChange = { isChecked ->
                    isDeleteMap[todo] = isChecked
                })
            }
        }
    }
}