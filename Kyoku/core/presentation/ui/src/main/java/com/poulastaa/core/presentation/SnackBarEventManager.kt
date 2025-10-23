package com.poulastaa.core.presentation

import com.poulastaa.core.presentation.designsystem.UiText
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SnackBarEventManager @Inject constructor() {
    private val _uiEvent = MutableSharedFlow<SnackBarUiEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val rootEvent = _uiEvent.asSharedFlow()

    fun showEvent(message: SnackBarUiEvent) {
        _uiEvent.tryEmit(message)
    }
}