package com.example.quickpoll.ui.screens.register_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.AuthRepository
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
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(value: String): Unit = _email.update { value }
    fun setPassword(value: String): Unit = _password.update { value }
    fun setName(value: String): Unit = _name.update { value }

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    fun handleRegister(
        navigate: () -> Unit
    ) {
        viewModelScope.launch {
            authRepository.register(email = email.value, password = password.value, name = name.value).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { UiState.LOADING }
                    }

                    is Resource.Success -> {
                        result.data.also { res ->
                            if (res.isSuccessful) {
                                showToast(applicationContext, res.body()?.message ?: "Registration successful, Now Login!")
                                _uiState.update { UiState.SUCCESS }
                                navigate()
                            }else{
                                showToast(applicationContext, res.message())
                                _uiState.update { UiState.ERROR }
                            }
                        }
                    }

                    is Resource.Error -> {
                        result.message?.let {
                            Log.e("REGISTER_ERROR", it)
                            showToast(applicationContext, it)
                        }
                        _uiState.update { UiState.ERROR }
                    }
                }
            }
        }
    }
}