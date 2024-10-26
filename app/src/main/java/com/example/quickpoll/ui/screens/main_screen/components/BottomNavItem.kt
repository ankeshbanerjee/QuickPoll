package com.example.quickpoll.ui.screens.main_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quickpoll.R

@Composable
fun BottomNavItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    title: String,
    icon: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Icon(
            painterResource(icon),
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        )
    }
}

@Preview
@Composable
private fun BottomNavItemPreview() {
    BottomNavItem(
        isSelected = true,
        onClick = {},
        title = "Polls",
        icon = R.drawable.ic_poll
    )
}