package com.example.quickpoll.ui.common.shared_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomAppBar() {
    CustomAppBarContent()
}

@Composable
private fun CustomAppBarContent() {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text("QuickPoll", style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.SemiBold
        ))
    }
}


@Preview
@Composable
private fun CustomAppBarPreview() {
    CustomAppBarContent()
}