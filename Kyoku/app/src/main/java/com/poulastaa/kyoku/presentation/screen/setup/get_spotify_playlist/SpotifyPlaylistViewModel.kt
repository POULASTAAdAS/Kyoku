package com.poulastaa.kyoku.presentation.screen.setup.get_spotify_playlist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.DsState
import com.poulastaa.kyoku.data.model.api.service.HandleSpotifyPlaylistStatus
import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.model.auth.UiEvent
import com.poulastaa.kyoku.data.model.database.table.PlaylistRelationTable
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiEvent
import com.poulastaa.kyoku.data.model.setup.get_spotify_playlist.GetSpotifyPlaylistUiState
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.utils.extractTokenOrCookie
import com.poulastaa.kyoku.utils.generatePlaylistName
import com.poulastaa.kyoku.utils.populateDsState
import com.poulastaa.kyoku.utils.readPlaylistFromDatabase
import com.poulastaa.kyoku.utils.setCookie
import com.poulastaa.kyoku.utils.storeCookieOrAccessToken
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toListOfSongTable
import com.poulastaa.kyoku.utils.toPlaylistTable
import com.poulastaa.kyoku.utils.toSpotifyPlaylistId
import com.poulastaa.kyoku.utils.validateSpotifyLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.CookieManager
import javax.inject.Inject

@HiltViewModel
class SpotifyPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val cm: CookieManager,
    private val api: ServiceRepository,
    private val db: DatabaseRepositoryImpl
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

    private var dsState by mutableStateOf(DsState())

    init {
        viewModelScope.launch {
            dsState = populateDsState(ds)

            Log.d("dsState", dsState.toString())

            if (dsState.authType === AuthType.UN_AUTH)
                storeSignInState(SignInStatus.AUTH, ds)
            if (dsState.authType == AuthType.GOOGLE_AUTH || dsState.authType == AuthType.PASSKEY_AUTH)
                setCookie(cm, dsState.tokenOrCookie)
        }
    }

    private val _playlist = MutableStateFlow<List<PlaylistRelationTable>>(emptyList())
    val playlist get() = _playlist.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _playlist.value = readPlaylistFromDatabase(db)
            Log.d("playlist", _playlist.value.toString())
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

            GetSpotifyPlaylistUiEvent.OnSkipClick -> {
                Log.d("playlist", _playlist.value.toString())
//                viewModelScope.launch(Dispatchers.IO) { // todo may change
//                    _uiEvent.send(UiEvent.Navigate(Screens.SetBirthDate.route))
//                }

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
            val response = api.getSpotifyPlaylist(
                tokenOrCookie = dsState.tokenOrCookie,
                playlistId = state.link.toSpotifyPlaylistId()
            )

            when (response.status) {
                HandleSpotifyPlaylistStatus.SUCCESS -> {
                    state = GetSpotifyPlaylistUiState(
                        isFirstPlaylist = false
                    )

                    try {
                        storeCookieOrAccessToken(cm.extractTokenOrCookie(), ds) // update cookie
                    } catch (_: Exception) {
                    }

                    handleResponseData(data = response.listOfResponseSong)
                }

                HandleSpotifyPlaylistStatus.FAILURE -> {
                    onEvent(GetSpotifyPlaylistUiEvent.EmitToast("Opp's something went wrong"))
                    state = GetSpotifyPlaylistUiState()
                }
            }
            // todo add network interceptor 1
            // todo check header problem on api
        }
    }

    private fun handleResponseData(data: List<ResponseSong>) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = ArrayList<Long>()

            data.toListOfSongTable().forEach {
                list.add(db.insertSong(it)) // collecting ids
            }

            val name = generatePlaylistName()

            list.forEach {
                db.insertSongIntoPlaylist(it.toPlaylistTable(name))
            }
        }
    }
}