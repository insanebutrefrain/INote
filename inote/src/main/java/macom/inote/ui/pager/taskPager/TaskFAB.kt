package macom.inote.ui.pager.taskPager

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

@Composable
fun TaskFAB(viewModel: INoteViewModel, navController: NavHostController) {
    FloatingActionButton(modifier = Modifier.clip(shape = CircleShape), onClick = {
        taskIsAdd.value = true
    }) {
        Icon(Icons.Filled.Add, contentDescription = "添加任务", modifier = Modifier.size(50.dp))
    }
}