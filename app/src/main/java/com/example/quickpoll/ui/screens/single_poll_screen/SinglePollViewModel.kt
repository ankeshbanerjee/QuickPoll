package com.example.quickpoll.ui.screens.single_poll_screen

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.quickpoll.SinglePoll
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.PollRepository
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
class SinglePollViewModel @Inject constructor(
    private val pollRepository: PollRepository,
    private val userRepository: UserRepository,
    @ApplicationContext private val appContext: Context,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val pollId = savedStateHandle.toRoute<SinglePoll>().id

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    private val _poll = MutableStateFlow<Poll?>(null)
    val poll = _poll.asStateFlow()

    private fun loadPoll(){
        viewModelScope.launch {
            pollRepository.getPollById(pollId).collect {resource ->
                when(resource){
                    is Resource.Success -> {
                        val response = resource.data
                        val responseBody = response.body()
                        if(response.isSuccessful && responseBody != null){
                            _poll.update { responseBody.result.poll }
                            userRepository.getUser().collect { res ->
                                when (res) {
                                    is Resource.Success -> {
                                        res.data.also { data->
                                            _user.update {
                                                data.body()?.result?.user
                                            }
                                        }
                                        _uiState.value = UiState.SUCCESS
                                    }

                                    is Resource.Error -> {
                                        res.message?.let {msg ->
                                            Log.d("GET_USER_ERROR", msg)
                                            showToast(appContext, msg)
                                        }
                                    }

                                    is Resource.Loading -> {

                                    }
                                }
                            }

                            _uiState.update { UiState.SUCCESS }
                        }
                    }
                    is Resource.Error -> {
                        Log.e("POLL_BY_ID_ERROR", resource.message ?: "Error loading poll")
                        _uiState.update { UiState.ERROR }
                    }
                    is Resource.Loading -> {
                        _uiState.update { UiState.LOADING }
                    }
                }

            }
        }
    }

    init {
        loadPoll()
    }

}