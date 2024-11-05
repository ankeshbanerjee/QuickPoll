package com.example.quickpoll.ui.screens.bottom_tabs.polls_tab

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.PollRepository
import com.example.quickpoll.utils.Constants.Companion.PAGE_LIMIT
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

    private val _page = MutableStateFlow(1)

    private val _hasMore = MutableStateFlow(false)

    init {
        loadPolls()
    }

    private fun resetValues() {
        _page.update { 1 }
        _hasMore.update { false }
        _polls.update { emptyList() }
    }

    fun refreshPolls() {
        resetValues()
        viewModelScope.launch {
            pollRepository.getAllPolls(
                page = _page.value,
                limit = PAGE_LIMIT
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            val responseBody = res.body()
                            if (responseBody == null) {
                                Log.e("GET_POLLS_ERROR", "Response body is null")
                                return@collect
                            }
                            _polls.update {
                                responseBody.result.polls
                            }
                            _page.update { it + 1 }
                            _hasMore.update { responseBody.result.hasNextPage }
                            Log.d("REFRESH_POLLS", "${_page.value} ${_hasMore.value} ${_polls.value[0].totalVotes}")
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
                        _uiState.update { UiState.REFRESHING }
                    }
                }
            }
        }
    }

    fun loadPolls(
    ) {
        if (_page.value != 1 && !_hasMore.value)
            return
        viewModelScope.launch {
            pollRepository.getAllPolls(
                page = _page.value,
                limit = PAGE_LIMIT
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            val responseBody = res.body()
                            if (responseBody == null) {
                                Log.e("GET_POLLS_ERROR", "Response body is null")
                                return@collect
                            }
                            _polls.update {
                                val newPolls = responseBody.result.polls
                                if (_page.value == 1)
                                    newPolls
                                else
                                    it + newPolls
                            }
                            _page.update { it + 1 }
                            _hasMore.update { responseBody.result.hasNextPage }
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