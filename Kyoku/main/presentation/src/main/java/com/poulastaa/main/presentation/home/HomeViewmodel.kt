package com.poulastaa.main.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = HomeUiState()
    )

    private val _uiEvent = Channel<HomeUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(isInitial: Boolean) {

    }

    fun onAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.OnSavedItemCLick -> {

            }
        }
    }
}