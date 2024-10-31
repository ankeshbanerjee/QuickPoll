package com.example.quickpoll.data.network.service

import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.model.poll.PollResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

data class CreatePollRequestBody(
    val question: String,
    val options: List<Option>,
    val expiry: String,
)

data class Option(
    val text: String
)

data class VotePollRequestBody(
    val pollId: String,
    val option: Int
)

interface PollService {
    @POST("poll/create")
    suspend fun createPoll(@Body requestBody: CreatePollRequestBody): Response<ApiResponse<Any>>

    @GET("poll/all")
    suspend fun getAllPolls(): Response<ApiResponse<PollResult>>

    @PATCH("poll/vote")
    suspend fun votePoll(@Body requestBody: VotePollRequestBody): Response<ApiResponse<Any>>

    @PATCH("poll/unvote")
    suspend fun unvotePoll(@Body requestBody: VotePollRequestBody): Response<ApiResponse<Any>>
}