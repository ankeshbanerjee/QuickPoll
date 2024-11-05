package com.example.quickpoll.ui.screens.bottom_tabs.polls_tab

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.quickpoll.data.network.model.poll.Option
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.ui.common.shared_components.CustomAppBar
import com.example.quickpoll.ui.common.shared_components.EndlessLazyColumn
import com.example.quickpoll.ui.common.shared_components.LoadingComponent
import com.example.quickpoll.ui.common.shared_viewmodels.UserViewModel
import com.example.quickpoll.ui.screens.bottom_tabs.polls_tab.components.PollComponent
import com.example.quickpoll.ui.screens.bottom_tabs.polls_tab.components.PollComponentViewModel
import com.example.quickpoll.utils.UiState

@Composable
fun PollsTabScreen(viewModel: PollsTabViewModel, userViewModel: UserViewModel) {
    val polls by viewModel.polls.collectAsStateWithLifecycle()
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val loadPolls = viewModel::loadPolls
    val refresh = viewModel::refreshPolls
    PollsTabScreenContent(
        polls = polls,
        user = user,
        uiState = uiState,
        loadPolls = loadPolls,
        refreshPolls = refresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PollsTabScreenContent(
    polls: List<Poll>,
    user: User?,
    uiState: UiState,
    loadPolls: () -> Unit,
    refreshPolls: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        CustomAppBar()
        PullToRefreshBox(
            isRefreshing = uiState == UiState.REFRESHING,
            onRefresh = refreshPolls,
            modifier = Modifier.fillMaxSize()
        ) {
            EndlessLazyColumn(
                items = polls,
                itemKey = { poll: Poll -> poll._id },
                itemContent = { poll ->
                    // args of viewmodel not updating - sol. -> using launchedEffect;
                    val viewModel =
                        hiltViewModel<PollComponentViewModel, PollComponentViewModel.PollComponentViewModelFactory>(
                            key = poll._id
                        ) { factory ->
                            factory.create(poll, user)
                        }
                    // update poll in viewmodel when it changes as viewmodel doesn't get re-instantiated on recomposition
                    LaunchedEffect(poll) {
                        viewModel.updatePoll(poll)
                    }
                    PollComponent(viewModel)
                },
                loadingItem = {
                    LoadingComponent()
                },
                loading = uiState == UiState.LOADING,
                refreshing = uiState == UiState.REFRESHING,
                loadMore = loadPolls,
                listEndContent = {
                    Spacer(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .height(40.dp)
                    )
                }
            )
        }
    }

}

@Preview
@Composable
private fun PollsTabScreenPreview() {
    PollsTabScreenContent(
        polls = listOf(
            Poll(
                _id = "67179c9c3794e6964de42948",
                question = "question 1",
                options = listOf(
                    Option(
                        text = "option 1",
                        votedBy = emptyList(),
                        _id = "67179c9c3794e6964de42949"
                    ),
                    Option(
                        text = "option 2",
                        votedBy = emptyList(),
                        _id = "67179c9c3794e6964de4294a"
                    ),
                    Option(
                        text = "option 3",
                        votedBy = emptyList(),
                        _id = "67179c9c3794e6964de4294b"
                    ),
                    Option(
                        text = "option 4",
                        votedBy = emptyList(),
                        _id = "67179c9c3794e6964de4294c"
                    )
                ),
                totalVotes = 0,
                createdBy = User(
                    _id = "67179c3f3794e6964de42943",
                    name = "Ankesh",
                    email = "a@g.co",
                    profilePic = "https://www.cornwallbusinessawards.co.uk/wp-content/uploads/2017/11/dummy450x450.jpg",
                    createdAt = "2024-10-22T12:36:15.106Z",
                    updatedAt = "2024-10-22T12:36:15.106Z",
                    __v = 0
                ),
                expiry = "2024-10-22T12:37:48.755Z",
                createdAt = "2024-10-22T12:37:48.763Z",
                updatedAt = "2024-10-22T12:49:29.491Z",
                __v = 4,
                image = null
            ),
            Poll(
                _id = "671e92250eb88bbde2827d1b",
                question = "question 1",
                options = listOf(
                    Option(
                        text = "option 1",
                        votedBy = emptyList(),
                        _id = "671e92250eb88bbde2827d1c"
                    ),
                    Option(
                        text = "option 2",
                        votedBy = emptyList(),
                        _id = "671e92250eb88bbde2827d1d"
                    ),
                    Option(
                        text = "option 3",
                        votedBy = emptyList(),
                        _id = "671e92250eb88bbde2827d1e"
                    ),
                    Option(
                        text = "option 4",
                        votedBy = emptyList(),
                        _id = "671e92250eb88bbde2827d1f"
                    )
                ),
                totalVotes = 0,
                createdBy = User(
                    _id = "67179c3f3794e6964de42943",
                    name = "Ankesh",
                    email = "a@g.co",
                    profilePic = "https://www.cornwallbusinessawards.co.uk/wp-content/uploads/2017/11/dummy450x450.jpg",
                    createdAt = "2024-10-22T12:36:15.106Z",
                    updatedAt = "2024-10-22T12:36:15.106Z",
                    __v = 0
                ),
                expiry = "2024-10-27T19:19:01.783Z",
                createdAt = "2024-10-27T19:19:01.796Z",
                updatedAt = "2024-10-27T19:19:01.796Z",
                __v = 0,
                image = "https://www.cornwallbusinessawards.co.uk/wp-content/uploads/2017/11/dummy450x450.jpg"
            ),
            Poll(
                _id = "671f528d98caa91ae0ebc48f",
                question = "How is everything going?",
                options = listOf(
                    Option(text = "Good", votedBy = emptyList(), _id = "671f528d98caa91ae0ebc490"),
                    Option(text = "Bad", votedBy = emptyList(), _id = "671f528d98caa91ae0ebc491"),
                    Option(
                        text = "Average",
                        votedBy = emptyList(),
                        _id = "671f528d98caa91ae0ebc492"
                    )
                ),
                totalVotes = 0,
                createdBy = User(
                    _id = "6718164fdd279ea7c3cd17d6",
                    name = "Ankesh",
                    email = "a2@g.co",
                    profilePic = "https://www.cornwallbusinessawards.co.uk/wp-content/uploads/2017/11/dummy450x450.jpg",
                    createdAt = "2024-10-22T21:17:03.516Z",
                    updatedAt = "2024-10-22T21:17:03.516Z",
                    __v = 0
                ),
                expiry = "2024-10-28T08:59:57.280Z",
                createdAt = "2024-10-28T08:59:57.293Z",
                updatedAt = "2024-10-28T08:59:57.293Z",
                __v = 0,
                image = null
            )
        ),
        user = null,
        uiState = UiState.IDLE,
        loadPolls = {},
        refreshPolls = {}
    )
}