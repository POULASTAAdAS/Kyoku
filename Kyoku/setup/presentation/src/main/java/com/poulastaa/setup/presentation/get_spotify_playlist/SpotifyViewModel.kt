package com.poulastaa.setup.presentation.get_spotify_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyViewModel @Inject constructor(

) : ViewModel() {
    var state by mutableStateOf(SpotifyPlaylistUiState())
        private set

    private val _uiEvent = Channel<SpotifyUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SpotifyPlaylistUiEvent) {
        when (event) {
            is SpotifyPlaylistUiEvent.OnLinkChange -> {
                state = state.copy(
                    link = state.link.copy(
                        data = event.data,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            SpotifyPlaylistUiEvent.OnLikeSubmit -> {
                // todo validate link

                state = state.copy(
                    isMakingApiCall = true
                )

                // todo api call
            }

            is SpotifyPlaylistUiEvent.OnPlaylistClick -> {

            }

            is SpotifyPlaylistUiEvent.UpdateCoverImage -> {

            }

            SpotifyPlaylistUiEvent.OnSkipClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(SpotifyUiAction.NavigateToSetBDate)
                }
            }
        }
    }
}