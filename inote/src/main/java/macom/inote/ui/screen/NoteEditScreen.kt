package macom.inote.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import macom.inote.data.Note
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 笔记编辑界面
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NoteEditScreen(
    navController: NavController,
    note: Note?,
    onSave: (title: String, content: String, modifyTime: Long, createTime: Long) -> Unit // 保存函数作为参数
) {
    // 当前标题和内容
    var title by remember { mutableStateOf(value = note?.title ?: "") }
    var content by remember { mutableStateOf(value = note?.body ?: "") }
    var lastModifiedTime by remember {
        mutableStateOf(
            value = note?.modifiedTime ?: System.currentTimeMillis()
        )
    }
    var createTime by remember { mutableStateOf(note?.createTime ?: System.currentTimeMillis()) }

    // 用于记录是否保存
    var isSaved by remember { mutableStateOf(true) }

    // 提示框状态
    var showUnsavedDialog by remember { mutableStateOf(false) }

    // 编辑历史栈
    val undoStack = remember { mutableStateListOf<Pair<String, String>>() }
    val redoStack = remember { mutableStateListOf<Pair<String, String>>() }

    // 输入框防止被遮挡
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("编辑笔记", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!isSaved) {
                            showUnsavedDialog = true
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 回退按钮
                    IconButton(
                        onClick = {
                            if (undoStack.isNotEmpty()) {
                                val lastState = undoStack.removeLast()
                                redoStack.add(title to content)
                                title = lastState.first
                                content = lastState.second
                                lastModifiedTime = System.currentTimeMillis()
                                isSaved = false
                            }
                        },
                        enabled = undoStack.isNotEmpty()
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "回退")
                    }
                    // 前进按钮
                    IconButton(
                        onClick = {
                            if (redoStack.isNotEmpty()) {
                                val nextState = redoStack.removeLast()
                                undoStack.add(title to content)
                                title = nextState.first
                                content = nextState.second
                                lastModifiedTime = System.currentTimeMillis()
                                isSaved = false
                            }
                        },
                        enabled = redoStack.isNotEmpty()
                    ) {
                        Icon(Icons.Default.ArrowForward, contentDescription = "前进")
                    }
                    // 保存按钮
                    IconButton(enabled = !isSaved, onClick = {
                        onSave(title, content, System.currentTimeMillis(), createTime)
                        isSaved = true
                        lastModifiedTime = System.currentTimeMillis()
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "保存")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding() // 防止键盘遮挡
        ) {
            // 可滚动的内容区域
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // 标题和正文一起滚动
            ) {
                // 显示最后修改的时间和日期
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text =
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(
                            lastModifiedTime
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    VerticalDivider(color = MaterialTheme.colorScheme.secondary)
                    Text(
                        text = "${content.length} 字符",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                // 标题输入框
                Text(
                    text = "标题",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
                BasicTextField(
                    value = title,
                    onValueChange = {
                        isSaved = false
                        undoStack.add(title to content)
                        title = it
                    }, maxLines = 1,
                    textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(16.dp)
                        .bringIntoViewRequester(bringIntoViewRequester) // 防止被遮挡
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        }
                )


                Spacer(modifier = Modifier.height(8.dp))

                // 正文输入框
                Text(
                    text = "正文",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                )
                BasicTextField(
                    value = content,
                    onValueChange = {
                        isSaved = false
                        undoStack.add(title to content)
                        content = it
                    },
                    textStyle = TextStyle(fontSize = 18.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(16.dp)
                        .wrapContentHeight() // 高度随内容变化
                        .bringIntoViewRequester(bringIntoViewRequester) // 防止被遮挡
                        .onFocusChanged { focusState ->
                            if (focusState.isFocused) {
                                coroutineScope.launch {
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // 提示框：未保存内容时
        if (showUnsavedDialog) {
            AlertDialog(
                onDismissRequest = { showUnsavedDialog = false },
                title = { Text("未保存的更改") },
                text = { Text("您有未保存的更改，确定要离开吗？") },
                confirmButton = {
                    TextButton(onClick = {
                        showUnsavedDialog = false
                        navController.popBackStack()
                    }) {
                        Text("离开")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUnsavedDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}