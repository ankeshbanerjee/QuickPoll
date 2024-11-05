package com.example.quickpoll.data.network.model.user

data class UserResult(
    val user: User
)

data class User(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val email: String,
    val name: String,
    val fcmTokens: List<String> = emptyList(),
    val profilePic: String,
    val updatedAt: String
)