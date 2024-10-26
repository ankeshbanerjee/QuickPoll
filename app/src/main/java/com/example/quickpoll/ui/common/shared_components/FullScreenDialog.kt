package com.example.quickpoll.ui.common.shared_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun FullScreenDialog(showDialog: Boolean) {
    if (showDialog) {
        Dialog(
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
            ),
            onDismissRequest = {},
        ) {
            Box(
//                modifier = Modifier
//                    .background(color = Color.Black.copy(alpha = 0.3f))
//                    .fillMaxWidth()
//                    .fillMaxSize(),
                contentAlignment = Alignment.Center
                ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun FullScreenDialogPreview() {
    FullScreenDialog(true)
}