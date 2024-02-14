package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.HandleSpotifyPlaylistStatus
import com.poulastaa.kyoku.data.model.api.service.SpotifyDataStoreState
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiState
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.insertIntoPlaylist
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toListOfUiPlaylist
import com.poulastaa.kyoku.utils.toSpotifyPlaylistId
import com.poulastaa.kyoku.utils.validateSpotifyLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val service: ServiceRepository,
    private val db: DatabaseRepositoryImpl
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = checkInternetConnection()
                )
            }
        }
    }

    private fun checkInternetConnection(): Boolean {
        return network.value == NetworkObserver.STATUS.AVAILABLE
    }

    var dsState by mutableStateOf(SpotifyDataStoreState())
        private set

    private fun readAccessToken() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                dsState = dsState.copy(
                    tokenOrCookie = it
                )
            }
        }
    }

    private fun readAuthType() {
        viewModelScope.launch {
            ds.readAuthType().collect {
                when (it) {
                    AuthType.SESSION_AUTH.name -> {
                        dsState = dsState.copy(
                            isCookie = true,
                            authType = AuthType.SESSION_AUTH
                        )
                        AuthType.SESSION_AUTH
                    }

                    AuthType.JWT_AUTH.name -> {
                        dsState = dsState.copy(
                            authType = AuthType.JWT_AUTH
                        )
                        AuthType.JWT_AUTH
                    }

                    else -> {
                        storeSignInState(SignInStatus.AUTH, ds)

                        AuthType.UN_AUTH
                    }
                }
            }
        }
    }

    init {
        readAccessToken()
        readAuthType()
        loadPlaylist()
    }

    private fun loadPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            db.getAllPlaylist().collect {
                state = state.copy(
                    listOfPlaylist = it.toListOfUiPlaylist()
                )
            }
        }
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

            is GetSpotifyPlaylistUiEvent.OnPlaylistClick -> {
                state = state.copy(
                    listOfPlaylist = state.listOfPlaylist.map {
                        if (it.name == event.name) it.copy(isExpanded = !it.isExpanded)
                        else it
                    }
                )
            }

            GetSpotifyPlaylistUiEvent.OnContinueClick -> {
                if (state.listOfPlaylist.isEmpty()) onEvent(GetSpotifyPlaylistUiEvent.OnSkipClick)
                else storeSignInState(SignInStatus.OLD_USER, ds)
            }

            GetSpotifyPlaylistUiEvent.OnSkipClick -> {
                viewModelScope.launch(Dispatchers.IO) {
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
            val response = service.getSpotifyPlaylist(
                playlistId = state.link.toSpotifyPlaylistId(),
            )

            when (response.status) {
                HandleSpotifyPlaylistStatus.SUCCESS -> {
                    state = GetSpotifyPlaylistUiState(
                        listOfPlaylist = state.listOfPlaylist,
                        isFirstPlaylist = false
                    )

                    insertIntoPlaylist(db, response.listOfResponseSong)
                }

                HandleSpotifyPlaylistStatus.FAILURE -> {
                    onEvent(GetSpotifyPlaylistUiEvent.EmitToast("Your Session has expired please login again"))
                    state = state.copy(
                        link = "",
                        isMakingApiCall = false
                    )
                    storeSignInState(SignInStatus.AUTH, ds)
                }
            }
        }
    }
}