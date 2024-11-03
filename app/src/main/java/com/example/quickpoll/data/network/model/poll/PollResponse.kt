package com.example.quickpoll.data.network.model.poll

import com.example.quickpoll.data.network.model.user.User

data class PollResult(
    val polls: List<Poll>,
    val currentPage: Int,
    val total: Int,
    val hasNextPage: Boolean
)

data class Option(
    val _id: String,
    val text: String,
    val votedBy: List<User>
)

data class Poll(
    val __v: Int,
    val _id: String,
    val createdAt: String,
    val createdBy: User,
    val expiry: String,
    val options: List<Option>,
    val question: String,
    val totalVotes: Int,
    val updatedAt: String
)