package com.example.quickpoll.data.network.model

data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val result: T,
    val success: Boolean
)
