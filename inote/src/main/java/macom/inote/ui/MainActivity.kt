package macom.inote.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import macom.inote.ui.navigation.MainNavigation
import macom.inote.ui.theme.INotesTheme
import macom.inote.viewModel.INoteViewModel


class MainActivity : ComponentActivity() {
    private lateinit var viewModel: INoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel = ViewModelProvider(this).get(INoteViewModel::class.java)
        setContent {
            INotesTheme {
                MainNavigation(viewModel)
            }
        }
    }
}







