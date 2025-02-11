package com.example.quickpoll.ui.screens.bottom_tabs.profile_tab

import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.quickpoll.LocalParentNavController
import com.example.quickpoll.Login
import com.example.quickpoll.R
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.ui.common.shared_components.CustomAppBar
import com.example.quickpoll.ui.common.shared_components.EndlessLazyColumn
import com.example.quickpoll.ui.common.shared_components.FullScreenDialog
import com.example.quickpoll.ui.common.shared_components.LoadingComponent
import com.example.quickpoll.ui.common.shared_viewmodels.UserViewModel
import com.example.quickpoll.ui.common.shared_components.poll_component.PollComponent
import com.example.quickpoll.ui.common.shared_components.poll_component.PollComponentViewModel
import com.example.quickpoll.utils.UiState
import com.example.quickpoll.utils.getFileFromUri
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ProfileTabScreen(
    profileTabViewModel: ProfileTabViewModel,
    userViewModel: UserViewModel
) {
    val user by userViewModel.user.collectAsStateWithLifecycle()
    val uiState by profileTabViewModel.uiState.collectAsStateWithLifecycle()
    val uploadImage = profileTabViewModel::uploadImage
    val polls by profileTabViewModel.polls.collectAsStateWithLifecycle()
    val pollUiState by profileTabViewModel.pollsUiState.collectAsStateWithLifecycle()
    val loadPolls = profileTabViewModel::loadPolls
    val refreshPolls = profileTabViewModel::refreshPolls
    val logout = profileTabViewModel::logout
    val parentNavController = LocalParentNavController.current
    fun handleLogout(
    ) {
        logout(
            {
                parentNavController.navigate(Login) {
                    popUpTo(0)
                }
            }
        )
    }
    ProfileTabScreenContent(
        user = user,
        uiState = uiState,
        uploadImage = uploadImage,
        polls = polls,
        pollUiState = pollUiState,
        loadPolls = loadPolls,
        refreshPolls = refreshPolls,
        logout = ::handleLogout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTabScreenContent(
    user: User?,
    uiState: UiState,
    uploadImage: (file: File?) -> Unit,
    polls: List<Poll>,
    pollUiState: UiState,
    loadPolls: () -> Unit,
    refreshPolls: () -> Unit,
    logout: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(top = 12.dp),
    ) {
        CustomAppBar(
            title = "Your Profile",
            endComponent = {
                IconButton(
                    onClick = logout,
                )
                {
                    Icon(
                        painter = painterResource(R.drawable.ic_logout),
                        contentDescription = null
                    )
                }
            }
        )
        PullToRefreshBox(
            isRefreshing = pollUiState == UiState.REFRESHING,
            onRefresh = refreshPolls,
            modifier = Modifier.fillMaxSize()
        ) {
            EndlessLazyColumn(
                listHeaderContent = {
                    Column {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(vertical = 12.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(user?.profilePic)
                                        .build(),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer)

                                )
                                val context = LocalContext.current
                                val pickMedia =
                                    rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                                        if (uri != null) {
                                            val file =
                                                getFileFromUri(
                                                    contentResolver = context.contentResolver,
                                                    uri = uri
                                                )
                                            uploadImage(file)
                                        }
                                    }
                                IconButton(
                                    onClick = {
                                        pickMedia.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                        .align(Alignment.BottomEnd)
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_camera),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                        UserDetailItem(
                            label = "Name",
                            value = user!!.name,
                            icon = Icons.Outlined.AccountBox
                        )
                        UserDetailItem(
                            label = "Email",
                            value = user.email,
                            icon = Icons.Outlined.Email
                        )
                        UserDetailItem(
                            label = "Registered On",
                            value = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                LocalDateTime.parse(user.createdAt, DateTimeFormatter.ISO_DATE_TIME)
                                    .format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                            } else {
                                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                                val formatter = SimpleDateFormat("dd MMMM yyyy")
                                val formattedDate = formatter.format(parser.parse(user.createdAt))
                                formattedDate
                            },
                            icon = Icons.Outlined.Info,
                            showDivider = false
                        )
                        Text(
                            "Your Polls",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(vertical = 14.dp, horizontal = 20.dp)
                        )
                    }
                },
                items = polls,
                itemKey = { poll: Poll -> poll._id },
                itemContent = { poll ->
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
                loading = pollUiState == UiState.LOADING,
                refreshing = pollUiState == UiState.REFRESHING,
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
    FullScreenDialog(uiState == UiState.LOADING)
}

@Composable
private fun UserDetailItem(
    label: String,
    value: String,
    icon: ImageVector,
    showDivider: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 18.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                label, style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                value,
                style = TextStyle(color = MaterialTheme.colorScheme.secondary, fontSize = 16.sp)
            )
        }
    }
    if (showDivider)
        HorizontalDivider()
}

@Preview
@Composable
private fun ProfileTabScreenPreview() {
    ProfileTabScreenContent(
        user = User(
            _id = "6718164fdd279ea7c3cd17d6",
            name = "Ankesh",
            email = "a2@g.co",
            profilePic = "https://www.cornwallbusinessawards.co.uk/wp-content/uploads/2017/11/dummy450x450.jpg",
            createdAt = "2024-10-22T21:17:03.516Z",
            updatedAt = "2024-10-22T21:17:03.516Z",
            __v = 0
        ),
        uiState = UiState.SUCCESS,
        uploadImage = {},
        polls = emptyList(),
        pollUiState = UiState.IDLE,
        loadPolls = {},
        refreshPolls = {},
        logout = {}
    )
}