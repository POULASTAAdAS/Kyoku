package com.poulastaa.play.presentation.view_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class ViewArtistViewModel @Inject constructor() : ViewModel() {
    var state by mutableStateOf(ViewArtistUiState())
        private set

    private val _uiEvent = Channel<ViewArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadData(artistId: Long) {

    }

    fun onEvent(event: ViewArtistUiEvent) {

    }
}