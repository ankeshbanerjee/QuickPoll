package com.example.quickpoll.data.repository

import com.example.quickpoll.data.network.service.AuthService
import com.example.quickpoll.data.network.service.LoginRequestBody
import com.example.quickpoll.data.network.service.RegisterRequestBody
import com.example.quickpoll.data.network.utils.safeApiCall
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authService: AuthService
) {
    suspend fun login(email: String, password: String) =
        safeApiCall { authService.login(LoginRequestBody(email, password)) }

    suspend fun register(name: String, email: String, password: String) =
        safeApiCall { authService.register(RegisterRequestBody(name, email, password)) }
}