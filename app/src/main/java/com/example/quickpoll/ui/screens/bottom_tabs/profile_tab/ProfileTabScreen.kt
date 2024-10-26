package com.example.quickpoll.ui.screens.bottom_tabs.profile_tab

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
fun ProfileTabScreen() {
    ProfileTabScreenContent()
}


@Composable
private fun ProfileTabScreenContent() {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to ProfileTab!")
    }
}

@Preview
@Composable
private fun ProfileTabScreenPreview() {
    ProfileTabScreen()
}