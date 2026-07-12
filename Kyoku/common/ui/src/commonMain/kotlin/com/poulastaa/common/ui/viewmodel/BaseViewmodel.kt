package com.poulastaa.common.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewmodel<UI_STATE, ACTION, EVENT>(initialSate: UI_STATE) : ViewModel() {
    protected val _uiState = MutableStateFlow(initialSate)
    val uiState = _uiState.asStateFlow().wrap()

    private val _event = Channel<EVENT>(Channel.BUFFERED)
    val event = _event.receiveAsFlow().wrap()

    fun onAction(action: ACTION) = viewModelScope.launch { handleAction(action) }

    protected fun onEvent(event: EVENT) {
        viewModelScope.launch { _event.send(event) }
    }

    protected inline fun updateState(block: UI_STATE.() -> UI_STATE) {
        _uiState.update { it.block() }
    }

    protected abstract suspend fun handleAction(action: ACTION)
}