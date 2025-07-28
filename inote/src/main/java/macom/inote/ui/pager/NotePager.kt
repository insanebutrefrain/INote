package macom.inote.ui.pager

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import macom.inote.R
import macom.inote.data.Note
import macom.inote.ui.component.DeleteConfirmAlert
import macom.inote.ui.helper.formatTimestamp
import macom.inote.ui.pager.notePager.NoteCard
import macom.inote.ui.pager.notePager.NoteOrder
import macom.inote.ui.pager.notePager.NoteTopBar
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel

/**
 * 笔记页面设计
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun NotePager(
    bottomPadding: PaddingValues, navController: NavHostController, viewModel: INoteViewModel
) {
    val isDeleteAlert = remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState()
    val notes = state.value.notes
    val noteOrder = remember { mutableStateOf(NoteOrder.ByModifiedTime) }
    val isDeleteMode = remember { mutableStateOf(false) }
    val isDeleteMap = mutableStateMapOf<Note, Boolean>()
    notes.forEach {
        isDeleteMap[it] = false
    }
    val searchNotes = remember { SnapshotStateList<Note>() }
    val isShow = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(modifier = Modifier, topBar = {
        NoteTopBar(
            isShow = isShow,
            notes,
            searchNotes,
            isDeleteAlert,
            isDeleteMode,
            isDeleteMap,
            noteOrder = noteOrder,
            viewModel = viewModel
        )
    }) { topPadding ->
        DeleteConfirmAlert(isShowDeleteAlert = isDeleteAlert, onDismiss = {
            isDeleteAlert.value = false
        }, onDeleteConfirmed = {
            isDeleteMap.forEach { entry ->
                if (entry.value) {
                    isDeleteMap[entry.key] = false
                    val intent = INoteIntent.DeleteNote(entry.key)
                    viewModel.deleteNote(intent)
                }
            }
            isDeleteMode.value = false
            isDeleteAlert.value = false
        })
        LazyColumn(
            modifier = Modifier
                .padding(
                    bottom = bottomPadding.calculateBottomPadding(),
                    top = topPadding.calculateTopPadding()
                )
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isShow.value) items(searchNotes.sortedByDescending { if (noteOrder.value == NoteOrder.ByModifiedTime) it.modifiedTime else it.createTime }) { note ->
                NoteCard(
                    navController = navController,
                    note = note,
                    isDeleteMode = isDeleteMode,
                    isDeleteMap = isDeleteMap
                )
            }
            else items(notes.sortedByDescending { if (noteOrder.value == NoteOrder.ByModifiedTime) it.modifiedTime else it.createTime }) { note ->
                NoteCard(
                    navController = navController,
                    note = note,
                    isDeleteMode = isDeleteMode,
                    isDeleteMap = isDeleteMap
                )
            }
        }
    }
}


