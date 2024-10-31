package com.example.quickpoll.ui.screens.bottom_tabs.polls_tab

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.PollRepository
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
class PollsTabViewModel @Inject constructor(
    private val pollRepository: PollRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _polls = MutableStateFlow<List<Poll>>(emptyList())
    val polls = _polls.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            pollRepository.getAllPolls().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            _polls.update { res.body()?.result?.polls ?: emptyList() }
                        }
                        _uiState.update { UiState.IDLE }
                    }

                    is Resource.Error -> {
                        response.message?.let { msg ->
                            Log.d("GET_POLLS_ERROR", response.toString())
                            showToast(appContext, msg)
                        }
                        _uiState.update { UiState.ERROR }
                    }

                    is Resource.Loading -> {
                        _uiState.update { UiState.LOADING }
                    }
                }
            }
        }
    }
}