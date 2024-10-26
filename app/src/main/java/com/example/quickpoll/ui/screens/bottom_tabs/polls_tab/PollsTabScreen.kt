package com.example.quickpoll.ui.screens.bottom_tabs.polls_tab

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun PollsTabScreen() {
    PollsTabScreenContent()
}


@Composable
private fun PollsTabScreenContent() {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to PollsTab!")
    }
}

@Preview
@Composable
private fun PollsTabScreenPreview() {
    PollsTabScreen()
}