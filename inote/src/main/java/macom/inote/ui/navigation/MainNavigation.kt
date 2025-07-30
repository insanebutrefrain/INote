package macom.inote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import macom.inote.data.Note
import macom.inote.ui.screen.LoginScreen
import macom.inote.ui.screen.MainPagerScreen
import macom.inote.ui.screen.NoteEditScreen
import macom.inote.ui.screen.ProfileScreen
import macom.inote.ui.screen.RegisterScreen
import macom.inote.ui.screen.SplashScreen
import macom.inote.viewModel.INoteIntent
import macom.inote.viewModel.INoteViewModel

/**
 * 用于导航至各个界面
 * 包括主界面、笔记编辑界面
 */
@Composable
fun MainNavigation(viewModel: INoteViewModel) {
    val navController: NavHostController = rememberNavController()
    val startRoute =
        if (viewModel.getPsw() == null || viewModel.getUser() == null || viewModel.getUser()
                .equals(other = "") || viewModel.getPsw().equals(other = "")
        ) "login" else "mainPager"
    NavHost(navController = navController, startDestination = startRoute) {
        /**
         * 导航至各个界面
         */
        // 闪屏界面，确定登录状态
        // 已经弃用
        composable(route = "splash") {
            SplashScreen(navController = navController, viewModel = viewModel)
        }
        // 个人信息界面
        composable(route = "profile") {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        // 登录界面
        composable(route = "login") {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        // 注册界面
        composable(route = "register") {
            RegisterScreen(
                navController = navController, viewModel = viewModel
            )
        }
        // 主界面
        composable(route = "mainPager") {
            MainPagerScreen(viewModel = viewModel, navController = navController)
        }
        // 添加笔记界面
        composable("addNote") {
            NoteEditScreen(navController = navController,
                note = null,
                onSave = { title, content, modifiedTime, createTime ->
                    val intent = INoteIntent.EditNote(
                        title = title,
                        body = content,
                        modifiedTime = modifiedTime,
                        createTime = createTime,
                    )
                    viewModel.editNote(intent)
                })
        }
        // 编辑笔记界面
        composable(
            "editNote" + "/{note}", arguments = listOf(navArgument("note") {
                type = NavType.StringType
            })
        ) {
            val noteStr = it.arguments?.getString("note")
            val note = Gson().fromJson(noteStr, Note::class.java)
            NoteEditScreen(navController = navController,
                note = note,
                onSave = { title, content, modifiedTime, createTime ->
                    val intent = INoteIntent.EditNote(
                        title = title,
                        body = content,
                        modifiedTime = modifiedTime,
                        createTime = note.createTime
                    )
                    viewModel.editNote(intent)
                })
        }
    }
}