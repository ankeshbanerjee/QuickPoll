package com.example.quickpoll.ui.screens.main_screen.components

import com.example.quickpoll.R
import com.example.quickpoll.ui.screens.main_screen.Polls
import com.example.quickpoll.ui.screens.main_screen.Profile

sealed class BottomNavigationTabs<T>(
    val title: String,
    val route: T,
    val icon: Int
) {
    data object PollsTab: BottomNavigationTabs<Polls>("Polls", Polls, R.drawable.ic_poll)
    data object ProfileTab: BottomNavigationTabs<Profile>("Profile", Profile, R.drawable.ic_user)
}
