package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiState
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = GetSpotifyPlaylistUiState(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(GetSpotifyPlaylistUiState())
        private set

    fun onEvent(event: GetSpotifyPlaylistUiEvent) {
        when (event) {
            is GetSpotifyPlaylistUiEvent.OnLinkEnter -> {
                state = state.copy(
                    link = event.like,
                    isLinkError = false,
                    linkSupportingText = ""
                )
            }

            GetSpotifyPlaylistUiEvent.OnAddButtonClick -> {
                if (checkInternetConnection()) {
                    if (state.link.validateSpotifyLink()) {
                        state = state.copy(
                            isMakingApiCall = true
                        )
                        makeApiCall()
                    } else {
                        state = state.copy(
                            isLinkError = true,
                            linkSupportingText = "Not a spotify playlist link"
                        )
                    }
                } else {
                    onEvent(GetSpotifyPlaylistUiEvent.EmitToast("Please check your internet connection"))
                }
            }

            GetSpotifyPlaylistUiEvent.OnSkipClick -> {
                viewModelScope.launch(Dispatchers.IO) { // todo may change
                    _uiEvent.send(UiEvent.Navigate(Screens.SetBirthDate.route))
                }
            }

            is GetSpotifyPlaylistUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            GetSpotifyPlaylistUiEvent.SomethingWentWrong -> {
                onEvent(GetSpotifyPlaylistUiEvent.EmitToast("Opp's something went wrong"))
            }
        }
    }

    private fun makeApiCall() {
        // todo make api call

        // todo upon response
        state = GetSpotifyPlaylistUiState(
            isFirstPlaylist = false
        )
    }

    private fun String.validateSpotifyLink(): Boolean {
        return true
    }
}