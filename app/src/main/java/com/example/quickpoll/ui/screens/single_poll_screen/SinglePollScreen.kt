package com.example.quickpoll.ui.screens.single_poll_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickpoll.LocalParentNavController
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.ui.common.shared_components.LoadingComponent
import com.example.quickpoll.ui.common.shared_viewmodels.UserViewModel
import com.example.quickpoll.ui.common.shared_components.poll_component.PollComponent
import com.example.quickpoll.ui.common.shared_components.poll_component.PollComponentViewModel
import com.example.quickpoll.utils.UiState

@Composable
fun SinglePollScreen(
    viewModel: SinglePollViewModel,
    userViewModel: UserViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val poll by viewModel.poll.collectAsStateWithLifecycle()
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val userFromApi = viewModel.user.collectAsStateWithLifecycle()
    userFromApi.value?.let {
        userViewModel.updateUser(it)
    }
    val navController = LocalParentNavController.current
    fun goBack() {
        navController.popBackStack()
    }
    SinglePollScreenContent(
        uiState = uiState,
        poll = poll,
        user = user,
        goBack = ::goBack
    )
}

@Composable
private fun SinglePollScreenContent(
    uiState: UiState,
    poll: Poll?,
    user: User?,
    goBack: () -> Unit
) {
    Scaffold {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.clickable {
                        goBack()
                    })
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Poll", style = MaterialTheme.typography.headlineSmall)
            }
            if (uiState == UiState.LOADING) {
                LoadingComponent()
            } else {
                poll?.let { pollItem ->
                    user?.let {
                        val viewModel =
                            hiltViewModel<PollComponentViewModel, PollComponentViewModel.PollComponentViewModelFactory>(
                                key = pollItem._id
                            ) { factory ->
                                factory.create(pollItem, it)
                            }
                        PollComponent(viewModel)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SinglePollScreenPreview() {
    SinglePollScreenContent(
        uiState = UiState.SUCCESS,
        poll = null,
        user = null,
        goBack = {}
    )
}
