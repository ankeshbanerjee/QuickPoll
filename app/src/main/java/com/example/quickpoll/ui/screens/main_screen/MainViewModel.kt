package com.example.quickpoll.ui.screens.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.UserRepository
import com.example.quickpoll.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
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
                        response.data?.let { res ->
                            if (res.isSuccessful) {
                                _user.update { res.body()?.result?.user }
                            }
                        }
                        _uiState.value = UiState.SUCCESS
                    }

                    is Resource.Error -> {
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