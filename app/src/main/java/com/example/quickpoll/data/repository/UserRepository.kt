package com.example.quickpoll.data.repository

import com.example.quickpoll.data.network.service.UserService
import com.example.quickpoll.data.network.utils.safeApiCall
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
){
    suspend fun getUser() = safeApiCall { userService.getUser() }
}