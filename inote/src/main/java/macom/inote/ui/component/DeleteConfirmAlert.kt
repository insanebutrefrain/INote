package macom.inote.ui.component

import android.annotation.SuppressLint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview

/**
 * 确认删除的弹窗
 */
@Composable
fun DeleteConfirmAlert(
    isShowDeleteAlert: MutableState<Boolean>,
    onDismiss: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    if (isShowDeleteAlert.value) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "确认删除") },
            text = { Text(text = "您确定要删除选中的内容吗?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteConfirmed() // 确认删除
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onDismiss()
                    }
                ) {
                    Text("取消")
                }
            }
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun DeleteConfirmAlertPreview() {
    DeleteConfirmAlert(mutableStateOf(true), {}, {})
}
