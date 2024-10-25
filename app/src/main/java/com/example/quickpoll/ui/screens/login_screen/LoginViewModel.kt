package com.example.quickpoll.ui.screens.login_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.AuthRepository
import com.example.quickpoll.utils.PreferencesDataStoreHelper
import com.example.quickpoll.utils.PreferencesDataStoreKey
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
class LoginViewModel @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun setEmail(value: String): Unit = _email.update { value }
    fun setPassword(value: String): Unit = _password.update { value }

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    fun handleLogin(
        navigate: () -> Unit
    ) {
        Log.d("LOGIN", "Email: ${email.value}, Password: ${password.value}")
        viewModelScope.launch {
            authRepository.login(email = email.value, password = password.value).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { UiState.LOADING }
                    }

                    is Resource.Success -> {
                        result.data?.let { res ->
                            if (res.isSuccessful) {
                                val responseBody = res.body()
                                val authToken = responseBody?.result?.token ?: return@collect
                                Log.d("AUTH_TOKEN", authToken)
                                PreferencesDataStoreHelper.storeData(
                                    applicationContext,
                                    key = PreferencesDataStoreKey.AUTH_TOKEN,
                                    data = authToken
                                )
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
                            Log.e("LOGIN_ERROR", it)
                            showToast(applicationContext, it)
                        }
                        _uiState.update { UiState.ERROR }
                    }
                }
            }
        }
    }


}