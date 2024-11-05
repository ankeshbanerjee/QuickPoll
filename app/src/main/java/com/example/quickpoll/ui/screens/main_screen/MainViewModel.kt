package com.example.quickpoll.ui.screens.main_screen

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.UserRepository
import com.example.quickpoll.utils.UiState
import com.example.quickpoll.utils.showToast
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUser().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            _user.update {
                                res.body()?.result?.user
                            }
                        }
                        _uiState.value = UiState.SUCCESS
                    }

                    is Resource.Error -> {
                       response.message?.let {msg ->
                           Log.d("GET_USER_ERROR", msg)
                            showToast(appContext, msg)
                       }
                        _uiState.value = UiState.ERROR
                    }

                    is Resource.Loading -> {
                        _uiState.value = UiState.LOADING
                    }
                }
            }
        }
        viewModelScope.launch {
            val fcmToken = Firebase.messaging.token.await()
            userRepository.saveFcmToken(fcmToken).collect {
                when (it) {
                    is Resource.Success -> {
                        Log.d("SAVE_FCM_SUCCESS", it.data.toString())
                    }

                    is Resource.Error -> {
                        Log.e("SAVE_FCM_ERROR", it.message.toString())
                    }

                    is Resource.Loading -> {
                        Log.d("SAVE_FCM_LOADING", "Loading")
                    }
                }
            }
        }
    }
}