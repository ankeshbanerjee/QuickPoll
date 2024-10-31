package com.example.quickpoll.ui.screens.main_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.UserRepository
import com.example.quickpoll.utils.UiState
import com.example.quickpoll.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    }
}