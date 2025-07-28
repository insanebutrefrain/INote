package macom.inote.ui.pager.taskPager

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
import macom.inote.data.Task
import macom.inote.ui.pager.isEditTask
import macom.inote.ui.pager.selectEditTask
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel

/**
 * 单个任务卡片
 */
@SuppressLint("UnrememberedMutableState")
@Composable
fun TaskCard(
    task: Task,
    viewModel: INoteViewModel,
    isDeleteMode: MutableState<Boolean>,
    isDeleteMap: SnapshotStateMap<Task, Boolean>
) {
    // 用于动画的透明度变化
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (task.isOver) 0.6f else 1.0f)
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    // 长按事件的处理
                    isDeleteMode.value = true  // 进入删除模式
                    isDeleteMap[task] = true   // 选中当前笔记
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
                modifier = Modifier
                    .animateContentSize(), // 动态变化的动画
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 让 Checkbox 在最左边
                Checkbox(
                    modifier = Modifier.alpha(if (isDeleteMode.value) 0f else 1.0f),
                    checked = task.isOver,
                    onCheckedChange = {
                        val intent = INoteIntent.CheckTask(task)
                        viewModel.checkTask(intent)
                    }
                )
                // Text 占据剩余空间
                Text(
                    text = task.body,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable {
                            isEditTask.value = true
                            selectEditTask.value = task
                        }
                )
            }
            // 让 Checkbox 在最右边，当处于删除模式时才显示
            if (isDeleteMode.value) {
                Checkbox(
                    checked = isDeleteMap[task] ?: false,
                    onCheckedChange = { isChecked ->
                        isDeleteMap[task] = isChecked
                    }
                )
            }
        }
    }
}