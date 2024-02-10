package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.database.AppDao
import com.poulastaa.kyoku.data.database.AppDatabase
import com.poulastaa.kyoku.data.model.api.service.HandleSpotifyPlaylistStatus
import com.poulastaa.kyoku.data.model.api.service.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.database.table.PlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiState
import com.poulastaa.kyoku.data.remote.ServiceApi
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.toSpotifyPlaylistId
import com.poulastaa.kyoku.utils.validateSpotifyLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    val ds: DataStoreOperation,
    private val api: ServiceRepository,
    private val db: AppDao
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

    private var tokenOrCookie: String? = null

    private fun readTokenOrCookie() {
        viewModelScope.launch(Dispatchers.IO) {
            ds.readTokenOrCookie().collect {
                tokenOrCookie = it
            }
        }
    }

    private val _playlist = MutableStateFlow<List<PlaylistRelationTable>>(emptyList())
    val playlist get() = _playlist.asStateFlow()

    private fun readAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getAllPlaylist().collect {
                _playlist.value = it
            }
        }
    }

    init {
        readTokenOrCookie()
        readAllData()
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
        viewModelScope.launch(Dispatchers.IO) {
            val response = if (tokenOrCookie != null)
                api.getSpotifyPlaylist(
                    tokenOrCookie = tokenOrCookie!!,
                    playlistId = state.link.toSpotifyPlaylistId()
                )
            else null

            if (response == null) {
                onEvent(GetSpotifyPlaylistUiEvent.EmitToast("Opp's something went wrong"))
                return@launch
            }

            state = GetSpotifyPlaylistUiState(
                isFirstPlaylist = false
            )

            // todo store cookie properly 1
            // todo add network interceptor first 2
            // todo add data class state for response and cookie 3
        }
    }
}