package macom.inote.ui.pager.notePager

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import macom.inote.viewModel.INoteViewModel

/**
 * 笔记页面 悬浮按钮
 */
@Composable
fun NoteFAB(viewModel: INoteViewModel, navController: NavHostController) {
    FloatingActionButton(modifier = Modifier.clip(shape = CircleShape), onClick = {
        navController.navigate("addNote") {

        }
    }) {
        Icon(Icons.Filled.Add, contentDescription = "添加笔记", modifier = Modifier.size(50.dp))
    }
}