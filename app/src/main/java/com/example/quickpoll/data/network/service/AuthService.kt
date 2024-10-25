package com.example.quickpoll.data.network.service

import com.example.quickpoll.data.network.model.ApiResponse
import com.example.quickpoll.data.network.model.auth.LoginResult
import com.example.quickpoll.data.network.model.auth.RegisterResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequestBody(
    val email: String,
    val password: String
)

data class RegisterRequestBody(
    val name: String,
    val email: String,
    val password: String
)

interface AuthService {
    @POST("user/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody
    ): Response<ApiResponse<LoginResult>>

    @POST("user/register")
    suspend fun register(
        @Body requestBody: RegisterRequestBody
    ): Response<ApiResponse<RegisterResult>>
}