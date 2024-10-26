package com.example.quickpoll.data.network.service

import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.model.user.UserResult
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("user/me")
    suspend fun getUser(): Response<ApiResponse<UserResult>>
}