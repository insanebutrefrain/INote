package macom.inote.ui.pager

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import macom.inote.data.Note
import macom.inote.ui.component.DeleteConfirmAlert
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
    val isSearchNotes = remember { SnapshotStateList<Note>() }
    val isShow = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(modifier = Modifier, topBar = {
        NoteTopBar(
            isShow = isShow,
            notes = notes,
            isSearchNotes = isSearchNotes,
            isDeleteAlert = isDeleteAlert,
            isDeleteMode = isDeleteMode,
            isDeleteMap = isDeleteMap,
            noteOrder = noteOrder,
            viewModel = viewModel,
            navController = navController
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
            if (isShow.value) items(isSearchNotes.sortedByDescending { if (noteOrder.value == NoteOrder.ByModifiedTime) it.modifiedTime else it.createTime }) { note ->
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


