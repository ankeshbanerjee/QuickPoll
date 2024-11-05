package com.example.quickpoll.data.network.service

import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.poll.PollResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

data class CreatePollRequestBody(
    val question: String,
    val options: List<Option>,
    val image: String? = null,
    val expiry: String,
)

data class Option(
    val text: String
)

data class VotePollRequestBody(
    val pollId: String,
    val option: Int
)

data class SinglePollResult(
    val poll: Poll
)

interface PollService {
    @POST("poll/create")
    suspend fun createPoll(@Body requestBody: CreatePollRequestBody): Response<ApiResponse<Any>>

    @GET("poll/all")
    suspend fun getAllPolls(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ApiResponse<PollResult>>

    @GET("poll/my-polls")
    suspend fun getMyPolls(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ApiResponse<PollResult>>

    @GET("poll")
    suspend fun getPollById(
        @Query("id") id: String
    ): Response<ApiResponse<SinglePollResult>>


    @PATCH("poll/vote")
    suspend fun votePoll(@Body requestBody: VotePollRequestBody): Response<ApiResponse<Any>>

    @PATCH("poll/unvote")
    suspend fun unvotePoll(@Body requestBody: VotePollRequestBody): Response<ApiResponse<Any>>
}