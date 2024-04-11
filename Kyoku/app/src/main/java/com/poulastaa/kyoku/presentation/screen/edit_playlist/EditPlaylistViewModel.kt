package com.poulastaa.kyoku.presentation.screen.edit_playlist

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiEvent
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiPlaylist
import com.poulastaa.kyoku.data.model.screens.edit_playlist.EditPlaylistUiState
import com.poulastaa.kyoku.data.model.screens.edit_playlist.LoadingStatus
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl
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

    var state by mutableStateOf(EditPlaylistUiState())

    init {
        readAccessToken()
        readAuthType()
    }

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

    fun loadData(
        typeString: String,
        id: Long
    ) {
        Log.d("playlistId", id.toString())

        viewModelScope.launch(Dispatchers.IO) {
            db.readPlaylistPreview().collect { results ->
                results.groupBy {
                    it.id
                }.map {
                    EditPlaylistUiPlaylist(
                        id = it.key,
                        name = it.value[0].name,
                        totalSongs = it.value.size,
                        isSelected = it.value.map { song ->
                            if (song.id == id) true else null
                        }.firstOrNull() ?: false,
                        urls = it.value.map { song ->
                            song.coverImage
                        }.take(4)
                    )
                }.forEach {
                    Log.d("playlist", it.toString())
                }
            }
        }
    }

    private fun load(typeString: String) = when (typeString) {
        SongType.HISTORY_SONG.name -> SongType.HISTORY_SONG
        SongType.ARTIST_SONG.name -> SongType.ARTIST_SONG
        else -> {
            onEvent(EditPlaylistUiEvent.EmitToast("opp's something went wrong"))
            null
        }
    }.let {
        if (it == null) {
            state = state.copy(
                loadingStatus = LoadingStatus.ERR
            )

            return@let
        }

        viewModelScope.launch(Dispatchers.IO) {

        }
    }


    private fun getSong() {

    }

    fun onEvent(event: EditPlaylistUiEvent) {
        when (event) {
            is EditPlaylistUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            EditPlaylistUiEvent.SomethingWentWrong -> {
                onEvent(EditPlaylistUiEvent.EmitToast("Opp's something went wrong"))
            }

            EditPlaylistUiEvent.SearchClick -> {
                state = state.copy(
                    isSearchEnable = true
                )
            }

            is EditPlaylistUiEvent.SearchText -> {
                state = state.copy(
                    searchText = event.text
                )

                // todo search job
            }

            EditPlaylistUiEvent.CancelSearch -> {
                state = state.copy(
                    isSearchEnable = false,
                    searchText = ""
                )
            }

            is EditPlaylistUiEvent.PlaylistClick -> {
                state = state.copy(
                    data = state.data.map {
                        if (it.id == event.id) it.copy(isSelected = !it.isSelected)
                        else it
                    }
                )
            }

            EditPlaylistUiEvent.DoneClick -> {

            }

            EditPlaylistUiEvent.NewPlaylistClick -> {

            }

            is EditPlaylistUiEvent.BottomSheetClick -> {
                when (event) {
                    is EditPlaylistUiEvent.BottomSheetClick.YesClick -> {

                    }

                    EditPlaylistUiEvent.BottomSheetClick.NoClick -> {

                    }
                }
            }
        }
    }
}