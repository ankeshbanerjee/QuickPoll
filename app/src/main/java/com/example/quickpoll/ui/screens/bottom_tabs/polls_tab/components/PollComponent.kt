package com.example.quickpoll.ui.screens.bottom_tabs.polls_tab.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.quickpoll.R
import com.example.quickpoll.data.network.model.poll.Option
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.utils.roundOffDecimal
import kotlin.reflect.typeOf

@Composable
fun PollComponent(viewModel: PollComponentViewModel) {
    val pollItem by viewModel.poll.collectAsStateWithLifecycle()
    val isVoted by viewModel.isVoted.collectAsStateWithLifecycle()
    val votedIdx by viewModel.votedIndex.collectAsStateWithLifecycle()
    fun unvotePoll(optionIdx: Int) {
        viewModel.unvote(optionIdx)
    }

    fun votePoll(optionIdx: Int) {
        viewModel.vote(optionIdx)
    }
    PollComponentContent(
        poll = pollItem,
        isVoted = isVoted,
        votePoll = ::votePoll,
        unvotePoll = ::unvotePoll,
        votedIndex = votedIdx
    )
}

@Composable
private fun PollComponentContent(
    poll: Poll,
    isVoted: Boolean,
    votePoll: (optionIdx: Int) -> Unit,
    unvotePoll: (optionIdx: Int) -> Unit,
    votedIndex: Int
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(8.dp)
            )
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                ,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(poll.createdBy.profilePic)
                    .build(),
                placeholder = painterResource(R.drawable.ic_image),
                contentDescription = "profile picture of the poll creator",
                contentScale = ContentScale.Crop
            )
//            Image(
//                painter = rememberAsyncImagePainter(poll.createdBy.profilePic),
//                contentDescription = "profile picture of the poll creator",
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(CircleShape),
//                contentScale = ContentScale.Crop
//            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    poll.createdBy.name,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    poll.createdBy.email,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Column {
            if (poll.image != null) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(poll.image)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_image),
                    contentDescription = "profile picture of the poll creator",
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                poll.question,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 14.dp)
            )
            poll.options.forEachIndexed { idx, it ->
                Box(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    if (isVoted) {
                        val widthFraction = (it.votedBy.size.toFloat() / poll.totalVotes.toFloat())
                        Log.d(
                            "POLL_COMP",
                            "vote indic: $votedIndex, ${poll.question}, width: $widthFraction, isVotedByUser: ${votedIndex == idx}"
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    if (votedIndex == idx) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                                )
                                .fillMaxWidth(widthFraction)
                                .height(40.dp)
                        )
                    }
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .clickable {
                                if (!isVoted)
                                    votePoll(idx)
                            }
                            .clip(RoundedCornerShape(6.dp))
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.primaryContainer,
                                RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(it.text, style = MaterialTheme.typography.bodyMedium)
                            if(isVoted){
                                Text(
                                    "${roundOffDecimal(it.votedBy.size.toFloat() / poll.totalVotes.toFloat() * 100f)} %",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    poll.totalVotes.toString() + " votes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
                if (isVoted) {
                    Text(
                        "Undo",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            if (votedIndex != -1) {
                                unvotePoll(votedIndex)
                            }
                        }
                    )
                }
            }
        }
    }
}
@Preview
@Composable
private fun PollComponentPreview() {
    PollComponentContent(
        poll = Poll(
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
            image = "https://www.cornwallbusinessawards.co.uk/wp-content/uploads/2017/11/dummy450x450.jpg",
        ),
        isVoted = true,
        votePoll = {},
        unvotePoll = {},
        votedIndex = 0
    )
}