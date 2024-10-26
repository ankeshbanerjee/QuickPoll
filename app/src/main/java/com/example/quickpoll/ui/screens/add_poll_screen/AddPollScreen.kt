package com.example.quickpoll.ui.screens.add_poll_screen

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
fun AddPollScreen() {
    AddPollScreenContent()
}


@Composable
private fun AddPollScreenContent() {
    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background).fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to AddPoll!")
    }
}

@Preview
@Composable
private fun AddPollScreenPreview() {
    AddPollScreen()
}