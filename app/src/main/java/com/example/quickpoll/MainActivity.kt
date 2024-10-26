package com.example.quickpoll

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.ui.common.shared_viewmodels.UserViewModel
import com.example.quickpoll.ui.screens.add_poll_screen.AddPollScreen
import com.example.quickpoll.ui.screens.login_screen.LoginScreen
import com.example.quickpoll.ui.screens.login_screen.LoginViewModel
import com.example.quickpoll.ui.screens.main_screen.MainScreen
import com.example.quickpoll.ui.screens.main_screen.MainViewModel
import com.example.quickpoll.ui.screens.register_screen.RegisterScreen
import com.example.quickpoll.ui.screens.register_screen.RegisterViewModel
import com.example.quickpoll.ui.screens.splash_screen.SplashScreen
import com.example.quickpoll.ui.theme.QuickPollTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

val LocalParentNavController =
    compositionLocalOf<NavController> { error("Parent navController not found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickPollTheme {
                val navController = rememberNavController()
                val userViewModel = hiltViewModel<UserViewModel>()
                CompositionLocalProvider(LocalParentNavController provides navController) {
                    NavHost(startDestination = Splash, navController = navController) {
                        composable<Splash> { SplashScreen() }
                        composable<Login> {
                            val viewModel = hiltViewModel<LoginViewModel>()
                            LoginScreen(viewModel)
                        }
                        composable<Register> {
                            val viewModel = hiltViewModel<RegisterViewModel>()
                            RegisterScreen(viewModel)
                        }
                        composable<Main> {
                            val viewModel = hiltViewModel<MainViewModel>()
                            MainScreen(viewModel, userViewModel)
                        }
                        composable<AddPoll> {
                            AddPollScreen()
                        }
                    }
                }

            }
        }
    }
}

@Serializable
object Splash

@Serializable
object Login

@Serializable
object Register

@Serializable
object Main

@Serializable
object AddPoll