package com.example.quickpoll

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.ui.common.shared_viewmodels.UserViewModel
import com.example.quickpoll.ui.screens.add_poll_screen.AddPollScreen
import com.example.quickpoll.ui.screens.add_poll_screen.AddPollViewModel
import com.example.quickpoll.ui.screens.login_screen.LoginScreen
import com.example.quickpoll.ui.screens.login_screen.LoginViewModel
import com.example.quickpoll.ui.screens.main_screen.MainScreen
import com.example.quickpoll.ui.screens.main_screen.MainViewModel
import com.example.quickpoll.ui.screens.register_screen.RegisterScreen
import com.example.quickpoll.ui.screens.register_screen.RegisterViewModel
import com.example.quickpoll.ui.screens.single_poll_screen.SinglePollScreen
import com.example.quickpoll.ui.screens.single_poll_screen.SinglePollViewModel
import com.example.quickpoll.ui.screens.splash_screen.SplashScreen
import com.example.quickpoll.ui.theme.QuickPollTheme
import com.example.quickpoll.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

val LocalParentNavController =
    compositionLocalOf<NavController> { error("Parent navController not found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // this works as SafeAreaView
            Box(modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars)){
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
                                val viewModel = hiltViewModel<AddPollViewModel>()
                                AddPollScreen(viewModel)
                            }
                            composable<SinglePoll> (
                                deepLinks = listOf(
                                    navDeepLink <SinglePoll>(
                                        basePath = "https://${Constants.DEEP_LINK_DOMAIN}/poll"
                                    )
                                )
                            ){
                                val viewModel = hiltViewModel<SinglePollViewModel>()
                                SinglePollScreen(
                                    viewModel,
                                    userViewModel
                                )
                            }
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

@Serializable
data class SinglePoll(val id: String)