package macom.inote.ui.pager.notePager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import macom.inote.R
import macom.inote.data.Note
import macom.inote.ui.pager.Pager
import macom.inote.viewModel.INoteViewModel

/**
 * 笔记页面 顶部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTopBar(
    isShow: MutableState<Boolean>,
    notes: SnapshotStateList<Note>,
    isSearchNotes: MutableList<Note>,
    isDeleteAlert: MutableState<Boolean>,
    isDeleteMode: MutableState<Boolean>,
    isDeleteMap: SnapshotStateMap<Note, Boolean>,
    noteOrder: MutableState<NoteOrder>,
    viewModel: INoteViewModel,
    navController: NavHostController
) {
    val isExpand = remember { mutableStateOf(false) }
    // 搜索状态下的导航含
    if (isShow.value) {
        val focusRequester = remember { FocusRequester() }
        val softKeyboard = LocalSoftwareKeyboardController.current // 获取软键盘控制器
        var txt by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus() // 请求焦点
            softKeyboard?.show() // 显示软键盘
        }
        TopAppBar(
            navigationIcon = {
                Icon(
                    painter = painterResource(R.drawable.note),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(modifier = Modifier
                        .padding(8.dp)  // 增加一些边距，避免与其他组件挤在一起
                        .fillMaxWidth()  // 让输入框填充父容器的宽度
                        .focusRequester(focusRequester), value = txt, onValueChange = {
                        txt = it
                        isSearchNotes.clear()
                        notes.forEach { note ->
                            if (txt.isNotEmpty()) {
                                if (txt in note.body || txt in note.title) {
                                    isSearchNotes.add(note)
                                }
                            }
                        }
                    }, leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp) // 为图标添加一点内边距
                        )
                    }, singleLine = true, textStyle = TextStyle(
                        fontSize = 16.sp,  // 设置字体大小为 16sp，避免过大的字体超出框
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                        // 设置输入框的样式，使其看起来更加现代
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,  // 聚焦时的边框颜色
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),  // 非聚焦时的边框颜色
                        ), shape = MaterialTheme.shapes.small  // 使用小圆角形状
                    )

                }
            },
            actions = {
                TextButton(onClick = {
                    isShow.value = false
                }) {
                    Text("取消", modifier = Modifier.wrapContentWidth())
                }
            },
        )
    }
    // 编辑删除状态下的顶部导航栏
    else if (isDeleteMode.value) {
        TopAppBar(title = {
            TextButton(onClick = {
                isDeleteMode.value = false
                notes.forEach {
                    isDeleteMap[it] = false
                }
            }) {
                Text(text = "取消")
            }
        },
            // 导航图标
            navigationIcon = {

            }, actions = {
                TextButton(onClick = {
                    notes.forEach {
                        isDeleteMap[it] = true
                    }
                }) {
                    Text(text = "全选")
                }
                TextButton(onClick = {
                    isDeleteAlert.value = true
                }) {
                    Text("删除")
                }
            })
    } else {
        TopAppBar(title = { Text(Pager.Note.title) },
            // 导航图标
//            navigationIcon = {
//                Icon(painterResource(R.drawable.note_outlined), contentDescription = null)
//            },
            actions = {
                IconButton(onClick = {
                    isShow.value = true
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "搜索")
                }
                IconButton(onClick = {
                    isExpand.value = true
                }) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "更多")
                    NoteMenuView(
                        isExpand = isExpand,
                        isDeleteMode = isDeleteMode,
                        noteOrder = noteOrder,
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            })
    }
}
