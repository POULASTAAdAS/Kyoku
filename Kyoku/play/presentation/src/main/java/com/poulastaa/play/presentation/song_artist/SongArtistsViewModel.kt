package com.poulastaa.play.presentation.song_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.play.domain.DataLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongArtistsViewModel @Inject constructor(
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(SongArtistsUiState())
        private set

    private var _uiEvent = Channel<SongArtistsUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(songId: Long) {
        state = state.copy(
            loadingState = DataLoadingState.LOADING
        )

        viewModelScope.launch {
            // todo load artists


            state = state.copy(
                loadingState = DataLoadingState.LOADED
            )
        }
    }

    fun onEvent(event: SongArtistsUiEvent) {
        when (event) {
            is SongArtistsUiEvent.OnArtistClick -> {

            }
        }
    }

    fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }
}