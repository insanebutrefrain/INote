package macom.inote.ui.navigation

import android.annotation.SuppressLint
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

enum class Route(val str: String) {
    Splash(str = "splash"), Profile(str = "profile"), Login(str = "login"), Register(str = "register"),
    MainPager(str = "mainPager"), AddNote(str = "addNote"), EditNote(str = "editNote")
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun MainNavigation(viewModel: INoteViewModel) {
    val navController: NavHostController = rememberNavController()
    val startRoute =
        if (viewModel.getPsw() != null && viewModel.getUser() != null) "mainPager" else "login"

    NavHost(navController = navController, startDestination = startRoute) {
        /**
         * 导航至各个界面
         */
        // 闪屏界面，确定登录状态
        // 已经弃用
        composable(route = Route.Splash.str) {
            SplashScreen(navController = navController, viewModel = viewModel)
        }
        // 个人信息界面
        composable(route = Route.Profile.str) {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }
        // 登录界面
        composable(route = Route.Login.str) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        // 注册界面
        composable(route = Route.Register.str) {
            RegisterScreen(
                navController = navController, viewModel = viewModel
            )
        }
        // 主界面
        composable(route = Route.MainPager.str) {
            MainPagerScreen(viewModel = viewModel, navController = navController)
        }
        // 添加笔记界面
        composable(route = Route.AddNote.str) {
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
            route = Route.EditNote.str + "/{note}", arguments = listOf(navArgument("note") {
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