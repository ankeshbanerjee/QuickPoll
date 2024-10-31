package com.example.quickpoll.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.service.CreatePollRequestBody
import com.example.quickpoll.data.network.service.Option
import com.example.quickpoll.data.network.service.PollService
import com.example.quickpoll.data.network.service.VotePollRequestBody
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.network.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.time.LocalDateTime
import javax.inject.Inject

class PollRepository @Inject constructor(
    private val pollService: PollService
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createPoll(
        question: String,
        options: List<String>
    ): Flow<Resource<Response<ApiResponse<Any>>>> {
        return safeApiCall {
            val optionList = options.map {
                Option(text = it)
            }
            pollService.createPoll(
                CreatePollRequestBody(
                    question = question,
                    options = optionList,
                    expiry = LocalDateTime.now().toString()
                )
            )
        }
    }

    suspend fun getAllPolls() = safeApiCall { pollService.getAllPolls() }

    suspend fun votePoll(pollId: String, option: Int) = safeApiCall {
        pollService.votePoll(VotePollRequestBody(pollId, option))
    }

    suspend fun unvotePoll(pollId: String, option: Int) = safeApiCall {
        pollService.unvotePoll(VotePollRequestBody(pollId, option))
    }

}