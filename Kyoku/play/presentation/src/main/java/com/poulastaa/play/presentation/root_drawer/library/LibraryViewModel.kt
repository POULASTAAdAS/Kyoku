package com.poulastaa.play.presentation.root_drawer.library

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(LibraryUiState())
        private set

    private val _uiEvent = Channel<LibraryUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LibraryUiEvent) {

    }
}