package com.example.quickpoll.ui.screens.bottom_tabs.polls_tab.components

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quickpoll.data.network.model.poll.Poll
import com.example.quickpoll.data.network.model.user.User
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.PollRepository
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

@HiltViewModel(assistedFactory = PollComponentViewModel.PollComponentViewModelFactory::class)
class PollComponentViewModel @AssistedInject constructor(
    @Assisted private val pollInput: Poll,
    @Assisted private val user: User?,
    private val pollRepository: PollRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    @AssistedFactory
    interface PollComponentViewModelFactory {
        fun create(poll: Poll, user: User?): PollComponentViewModel
    }

    private val _poll = MutableStateFlow(pollInput)
    val poll = _poll.asStateFlow()


    private val _isVoted = MutableStateFlow(
//        pollInput.options.any { option ->
//        option.votedBy.any { voter ->
//            voter._id == user?._id
//        }
//    }
        false
    )
    val isVoted = _isVoted.asStateFlow()

    private val _votedIndex = MutableStateFlow(
//        pollInput.options.indexOfFirst { option ->
//            option.votedBy.any { voter ->
//                voter._id == user?._id
//            }
//        }
        -1
    )
    val votedIndex = _votedIndex.asStateFlow()

    init {
        _isVoted.update {
            pollInput.options.any { option ->
                option.votedBy.any { voter ->
                    voter._id == user?._id
                }
            }
        }
        if (isVoted.value) {
            _votedIndex.update {
                pollInput.options.indexOf(pollInput.options.find { option ->
                    option.votedBy.contains(user)
                })
            }
        }

        Log.d("POLL_VM", "${pollInput.question} : isVoted: ${isVoted.value}, votedIdx: ${votedIndex.value}")
    }

    fun vote(optionIndex: Int) {
        viewModelScope.launch {
            pollRepository.votePoll(pollInput._id, optionIndex).collect {
                when (it) {
                    is Resource.Success -> {
                        showToast(appContext, it.data.body()?.message ?: "Voted successfully")
                        _isVoted.update { true }
                        _votedIndex.update { optionIndex }
                        _poll.update { item ->
                            item.copy(
                                totalVotes = item.totalVotes + 1,
                                options = item.options.mapIndexed { index, option ->
                                    if (index == optionIndex) {
                                        option.copy(
                                            votedBy = option.votedBy.toMutableList().apply {
                                                add(user!!)
                                            }
                                        )
                                    } else {
                                        option
                                    }
                                }
                            )
                        }
                    }

                    is Resource.Error -> {
                        showToast(appContext, it.message ?: "Something went wrong")
                        return@collect
                    }

                    is Resource.Loading -> {
//                        showToast(appContext, "Voting...")
                    }
                }
            }
        }
    }

    fun unvote(optionIndex: Int) {
        viewModelScope.launch {
            pollRepository.unvotePoll(pollInput._id, optionIndex).collect {
                when (it) {
                    is Resource.Success -> {
                        showToast(appContext, it.data.body()?.message ?: "Unvoted successfully")
                        _isVoted.value = false
                        _votedIndex.update { -1 }
                        _poll.update { item ->
                            item.copy(
                                totalVotes = item.totalVotes - 1,
                                options = item.options.mapIndexed { index, option ->
                                    if (index == optionIndex) {
                                        option.copy(
                                            votedBy = option.votedBy.toMutableList().apply {
                                                remove(user!!)
                                            }
                                        )
                                    } else {
                                        option
                                    }
                                }
                            )
                        }
                    }

                    is Resource.Error -> {
                        showToast(appContext, it.message ?: "Something went wrong")
                        return@collect
                    }

                    is Resource.Loading -> {
//                        showToast(appContext, "Unvoting...")
                    }
                }
            }
        }
    }

}