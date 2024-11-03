package com.example.quickpoll.ui.screens.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.quickpoll.AddPoll
import com.example.quickpoll.LocalParentNavController
import com.example.quickpoll.R
import com.example.quickpoll.ui.common.shared_components.LoadingComponent
import com.example.quickpoll.ui.common.shared_viewmodels.UserViewModel
import com.example.quickpoll.ui.screens.bottom_tabs.polls_tab.PollsTabViewModel
import com.example.quickpoll.ui.screens.bottom_tabs.polls_tab.PollsTabScreen
import com.example.quickpoll.ui.screens.bottom_tabs.profile_tab.ProfileTabScreen
import com.example.quickpoll.ui.screens.main_screen.components.BottomNavItem
import com.example.quickpoll.utils.UiState
import kotlinx.serialization.Serializable


sealed class BottomNavigationTabs<T>(
    val title: String,
    val route: T,
    val icon: Int
) {
    data object PollsTab: BottomNavigationTabs<Polls>("Polls", Polls, R.drawable.ic_poll)
    data object ProfileTab: BottomNavigationTabs<Profile>("Profile", Profile, R.drawable.ic_user)
}

val LocalBottomNavController =
    compositionLocalOf<NavController> { error("Bottom navController not found!") }

@Composable
fun MainScreen(viewModel: MainViewModel, userViewModel: UserViewModel) {
    val userFromApi = viewModel.user.collectAsStateWithLifecycle()
    userFromApi.value?.let {
        userViewModel.updateUser(it)
    }
    val user = userViewModel.user.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MainScreenContent(
        uiState = uiState.value,
        userViewModel = userViewModel,
    )
}

@Composable
private fun MainScreenContent(
    uiState: UiState,
    userViewModel: UserViewModel,
) {
    if (uiState == UiState.LOADING) {
        LoadingComponent()
        return
    }
    val navController = rememberNavController()
    CompositionLocalProvider(LocalBottomNavController provides navController) {
        Scaffold(
            bottomBar = {
                val navStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navStackEntry?.destination
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .shadow(20.dp)
                        .border(
                            0.5.dp,
                            Color.LightGray,
                            RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                        )
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(vertical = 16.dp)
                ) {
                    BottomNavItem(
                        title = BottomNavigationTabs.PollsTab.title,
                        icon = BottomNavigationTabs.PollsTab.icon,
                        isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(
                                BottomNavigationTabs.PollsTab.route::class
                            )
                        } == true,
                        onClick = {
                            navController.navigate(BottomNavigationTabs.PollsTab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(60.dp)) // here size is nearly the size of the FAB
                    BottomNavItem(
                        title = BottomNavigationTabs.ProfileTab.title,
                        icon = BottomNavigationTabs.ProfileTab.icon,
                        isSelected = currentDestination?.hierarchy?.any {
                            it.hasRoute(
                                BottomNavigationTabs.ProfileTab.route::class
                            )
                        } == true,
                        onClick = {
                            navController.navigate(BottomNavigationTabs.ProfileTab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
                val parentNavController = LocalParentNavController.current
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                    LargeFloatingActionButton(
                        modifier = Modifier
                            .size(64.dp)
                            .offset(y = (-30).dp),
                        onClick = {
                            parentNavController.navigate(AddPoll)
                        },
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                    ) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController, startDestination = Polls, Modifier.padding(innerPadding)
            ) {
                composable<Polls> {
                    val viewModel = hiltViewModel<PollsTabViewModel>()
                    PollsTabScreen(viewModel, userViewModel)
                }
                composable<Profile> {
                    ProfileTabScreen()
                }
            }
        }
    }
}

@Serializable
data object Polls

@Serializable
data object Profile

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreenContent(
        UiState.IDLE,
        userViewModel = UserViewModel()
    )
}