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
        options: List<String>,
        image: String? = null
    ): Flow<Resource<Response<ApiResponse<Any>>>> {
        return safeApiCall {
            val optionList = options.map {
                Option(text = it)
            }
            pollService.createPoll(
                CreatePollRequestBody(
                    question = question,
                    options = optionList,
                    image = image,
                    expiry = LocalDateTime.now().toString()
                )
            )
        }
    }

    suspend fun getAllPolls(
        page: Int = 1,
        limit: Int = 10,
    ) = safeApiCall {
        pollService.getAllPolls(
            page = page,
            limit = limit
        )
    }

    suspend fun getMyPolls(
        page: Int = 1,
        limit: Int = 10,
    ) = safeApiCall {
        pollService.getMyPolls(
            page = page,
            limit = limit
        )
    }

    suspend fun getPollById(pollId: String) = safeApiCall {
        pollService.getPollById(pollId)
    }

    suspend fun votePoll(pollId: String, option: Int) = safeApiCall {
        pollService.votePoll(VotePollRequestBody(pollId, option))
    }

    suspend fun unvotePoll(pollId: String, option: Int) = safeApiCall {
        pollService.unvotePoll(VotePollRequestBody(pollId, option))
    }

}