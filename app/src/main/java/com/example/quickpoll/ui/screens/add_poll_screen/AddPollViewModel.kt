package com.example.quickpoll.ui.screens.add_poll_screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.PollRepository
import com.example.quickpoll.data.repository.UploadRepository
import com.example.quickpoll.utils.UiState
import com.example.quickpoll.utils.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddPollViewModel @Inject constructor(
    private val pollRepository: PollRepository,
    private val uploadRepository: UploadRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {
    private val _pollQuestion = MutableStateFlow("")
    val pollQuestion = _pollQuestion.asStateFlow()

    private val _options = MutableStateFlow(emptyList<String>())
    val options = _options.asStateFlow()

    private val _uiState = MutableStateFlow(UiState.IDLE)
    val uiState = _uiState.asStateFlow()

    private val _imageurl = MutableStateFlow<String?>(null)
    val imageurl = _imageurl.asStateFlow()

    fun setPollQuestion(question: String) {
        _pollQuestion.value = question
    }

    fun addOption(option: String) {
        val currOps = _options.value.toMutableList()
        currOps.add(option)
        _options.update { currOps }
    }

    fun deleteOption(option: String) {
        val currOps = _options.value.toMutableList()
        currOps.remove(option)
        _options.update { currOps }
    }

    fun uploadImage(file: File?){
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
                            Log.d("IMAGE_URL", url ?: "No URL")
                            _imageurl.update { url }
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createPoll(
        goBack: () -> Unit
    ) {
        if (pollQuestion.value.isEmpty()) {
            showToast(appContext, "Poll question cannot be empty!")
            return
        }
        if (options.value.size < 2) {
            showToast(appContext, "Poll options should be at least 2!")
            return
        }
        viewModelScope.launch {
            pollRepository.createPoll(question = _pollQuestion.value, options = _options.value, image = _imageurl.value).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val res = response.data
                        showToast(appContext, res.body()?.message ?: "Poll created!")
                        _uiState.update { UiState.SUCCESS }
                        goBack()
                    }

                    is Resource.Loading -> {
                        _uiState.update { UiState.LOADING }
                    }

                    is Resource.Error -> {
                        response.message?.let { msg ->
                            Log.e("CREATE_POLL_ERROR", msg)
                            showToast(appContext, msg)
                        }
                        _uiState.update { UiState.ERROR }
                    }
                }
            }
        }
    }

}