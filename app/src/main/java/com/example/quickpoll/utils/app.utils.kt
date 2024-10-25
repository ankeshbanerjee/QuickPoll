package com.example.quickpoll.utils

import android.content.Context
import android.widget.Toast

enum class UiState {
    LOADING,
    SUCCESS,
    ERROR,

    REFRESHING,
    IDLE,
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}