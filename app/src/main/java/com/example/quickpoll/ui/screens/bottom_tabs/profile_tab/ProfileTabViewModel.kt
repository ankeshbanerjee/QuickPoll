package com.example.quickpoll.ui.screens.bottom_tabs.profile_tab

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.PollRepository
import com.example.quickpoll.data.repository.UploadRepository
import com.example.quickpoll.data.repository.UserRepository
import com.example.quickpoll.utils.Constants.Companion.PAGE_LIMIT
import com.example.quickpoll.utils.UiState
import com.example.quickpoll.utils.showToast
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

@HiltViewModel(assistedFactory = ProfileTabViewModel.ProfileTabViewModelFactory::class)
class ProfileTabViewModel @AssistedInject constructor(
    private val uploadRepository: UploadRepository,
    private val userRepository: UserRepository,
    private val pollRepository: PollRepository,
    @Assisted private val updateUser: (user: User?) -> Unit,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    @AssistedFactory
    interface ProfileTabViewModelFactory {
        fun create(updateUser: (user: User?) -> Unit): ProfileTabViewModel
    }

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    private val _polls = MutableStateFlow<List<Poll>>(emptyList())
    val polls = _polls.asStateFlow()

    private val _pollsUiState = MutableStateFlow(UiState.IDLE)
    val pollsUiState = _pollsUiState.asStateFlow()

    private val _page = MutableStateFlow(1)

    private val _hasMore = MutableStateFlow(false)

    init {
        loadPolls()
    }

    fun loadPolls() {
        if (_page.value != 1 && !_hasMore.value)
            return
        viewModelScope.launch {
            pollRepository.getMyPolls(
                page = _page.value,
                limit = PAGE_LIMIT
            ).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            val responseBody = res.body()
                            if (responseBody == null){
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
                            _page.update { _page.value + 1 }
                            _hasMore.update { res.body()?.result?.hasNextPage ?: false }
                        }
                        _pollsUiState.update { UiState.IDLE }
                    }

                    is Resource.Error -> {
                        response.message?.let { msg ->
                            Log.d("GET_POLLS_ERROR", response.toString())
                            showToast(appContext, msg)
                        }
                        _pollsUiState.update { UiState.ERROR }
                    }

                    is Resource.Loading -> {
                        _pollsUiState.update { UiState.LOADING }
                    }
                }
            }
        }
    }

    fun uploadImage(file: File?) {
        if (file == null) {
            showToast(appContext, "File is null!")
            return
        }
        viewModelScope.launch {
            uploadRepository.upload(file).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        response.data.also { res ->
                            val responseBody = res.body()
                            showToast(appContext, responseBody?.message ?: "Image uploaded!")
                            val url = responseBody?.result?.url
                            userRepository.updateUser(profilePic = url).collect { response2 ->
                                when (response2) {
                                    is Resource.Success -> {
                                        response2.data.also { res2 ->
                                            val responseBody2 = res2.body()
                                            showToast(
                                                appContext,
                                                responseBody2?.message ?: "Profile picture updated!"
                                            )
                                            val user = responseBody2?.result?.user
                                            updateUser(user)
                                            _uiState.update { UiState.SUCCESS }
                                        }
                                    }

                                    is Resource.Error -> {
                                        response2.message?.let { msg ->
                                            Log.e("UPDATE_USER_ERROR", msg)
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

                    is Resource.Loading -> {
                        _uiState.update { UiState.LOADING }
                    }

                    is Resource.Error -> {
                        response.message?.let { msg ->
                            Log.e("UPLOAD_IMAGE_ERROR", msg)
                            showToast(appContext, msg)
                        }
                        _uiState.update { UiState.ERROR }
                    }
                }
            }
        }
    }

}