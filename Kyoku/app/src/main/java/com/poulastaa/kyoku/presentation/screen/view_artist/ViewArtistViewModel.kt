package com.poulastaa.kyoku.presentation.screen.view_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiArtist
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiEvent
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewArtistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val api: ServiceRepository,
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = it == NetworkObserver.STATUS.AVAILABLE,
                    isInternetError = false
                )
                if (!state.isInternetAvailable)
                    state = state.copy(
                        isInternetError = true
                    )
            }
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun readAccessToken() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(
                    headerValue = it
                )
            }
        }
    }

    private fun readAuthType() {
        viewModelScope.launch {
            val authType = ds.readAuthType().first()

            state = state.copy(
                isCookie = authType == AuthType.SESSION_AUTH.name
            )
        }
    }

    var state by mutableStateOf(ViewArtistUiState())
        private set

    init {
        readAccessToken()
        readAuthType()
    }

    fun getArtist(songId: Long) {
        if (state.artist.isEmpty())
            viewModelScope.launch(Dispatchers.IO) {
                val response = async { api.getArtistOnSongId(songId) }.await()

                state = if (response.isEmpty())
                    state.copy(
                        noArtist = true,
                        isLoading = false
                    )
                else
                    state.copy(
                        artist = response.map {
                            ViewArtistUiArtist(
                                artistId = it.id,
                                name = it.name,
                                coverImage = it.coverImage,
                                listened = it.points
                            )
                        }.sortedByDescending {
                            it.listened
                        },
                        isLoading = false
                    )
            }
    }

    fun onEvent(event: ViewArtistUiEvent) {
        when (event) {
            is ViewArtistUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            ViewArtistUiEvent.SomethingWentWrong -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(ViewArtistUiEvent.EmitToast("Opp's something went wrong"))
                }
            }

            is ViewArtistUiEvent.ArtistClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(
                        UiEvent.NavigateWithData(
                            route = Screens.AllFromArtist.route,
                            name = event.name
                        )
                    )
                }
            }
        }
    }
}