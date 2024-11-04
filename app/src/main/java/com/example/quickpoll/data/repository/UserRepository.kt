package com.example.quickpoll.data.repository

import com.example.quickpoll.data.network.service.UserService
import com.example.quickpoll.data.network.utils.safeApiCall
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun getUser() = safeApiCall { userService.getUser() }

    suspend fun updateUser(
        name: String? = null,
        email: String? = null,
        profilePic: String? = null
    ) = safeApiCall {
        val map = mutableMapOf<String, String>()
        name?.let { map["name"] = it }
        email?.let { map["email"] = it }
        profilePic?.let { map["profilePic"] = it }
        userService.updateUser(
            update = map
        )
    }
}