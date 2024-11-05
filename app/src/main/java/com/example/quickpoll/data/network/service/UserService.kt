package com.example.quickpoll.data.network.service

import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.model.user.UserResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

data class FcmTokenRequest(
    val token: String
)

interface UserService {
    @GET("user/me")
    suspend fun getUser(): Response<ApiResponse<UserResult>>

    @PATCH("user/update")
    suspend fun updateUser(
        @Body update: Map<String, String>
    ): Response<ApiResponse<UserResult>>

    @POST("user/save-token")
    suspend fun saveFcmToken(
        @Body fcmTokenRequest: FcmTokenRequest
    ): Response<ApiResponse<Any>>

    @POST("user/delete-token")
    suspend fun deleteFcmToken(
        @Body fcmTokenRequest: FcmTokenRequest
    ): Response<ApiResponse<Any>>
}