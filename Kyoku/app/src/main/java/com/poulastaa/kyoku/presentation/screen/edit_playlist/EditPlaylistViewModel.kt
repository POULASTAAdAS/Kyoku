package com.poulastaa.kyoku.presentation.screen.edit_playlist

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
import com.poulastaa.kyoku.data.model.screens.edit_playlist.UiFav
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl,
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

    var state by mutableStateOf(EditPlaylistUiState())

    private var searchJob: Job? = null

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

    fun loadData(id: Long) {
        state = state.copy(
            songId = id
        )

        viewModelScope.launch(Dispatchers.IO) {
            val playlist = async {
                db.readPlaylistPreview().collect { results ->
                    state = state.copy(
                        playlist = async {
                            results.groupBy {
                                it.playlistId
                            }.map {
                                EditPlaylistUiPlaylist(
                                    id = it.key,
                                    name = it.value[0].name,
                                    totalSongs = it.value.size,
                                    isSelected = it.value.map { song ->
                                        if (song.playlistId == id) true else null
                                    }.firstOrNull() ?: false,
                                    urls = it.value.map { song ->
                                        song.coverImage
                                    }.take(4)
                                )
                            }
                        }.await(),
                        loadingStatus = LoadingStatus.NOT_LOADING
                    )
                }
            }

            val fav = async {
                val temp = db.isInFavourite(id)

                db.countFavouriteSong().collect {
                    state = state.copy(
                        fav = UiFav(
                            totalSongs = it.sumOf { count ->
                                count.toInt()
                            },
                            isSelected = temp
                        )
                    )
                }
            }

            playlist.await()
            fav.await()
        }
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

            is EditPlaylistUiEvent.NewPlaylist -> {
                when (event) {
                    EditPlaylistUiEvent.NewPlaylist.NewPlaylistOpen -> {
                        state = state.copy(
                            isNewPlaylist = true
                        )
                    }

                    is EditPlaylistUiEvent.NewPlaylist.NewPlaylistNameEnter -> {
                        state = state.copy(
                            newPlaylistText = event.text
                        )
                    }

                    EditPlaylistUiEvent.NewPlaylist.NewPlaylistYes -> {

                    }


                    EditPlaylistUiEvent.NewPlaylist.NewPlaylistNo -> {
                        state = state.copy(
                            isNewPlaylist = false,
                            newPlaylistText = ""
                        )
                    }
                }
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

                searchJob?.cancel()
                searchJob = search(event.text)
            }

            EditPlaylistUiEvent.CancelSearch -> {
                state = state.copy(
                    isSearchEnable = false,
                    searchText = ""
                )

                search("")
            }

            is EditPlaylistUiEvent.PlaylistClick -> {
                state = state.copy(
                    playlist = state.playlist.map {
                        if (it.id == event.id) it.copy(
                            totalSongs = if (!it.isSelected) it.totalSongs + 1 else it.totalSongs - 1,
                            isSelected = !it.isSelected,
                        )
                        else it
                    }
                )
            }

            is EditPlaylistUiEvent.FavClick -> {
                val temp = state.fav.isSelected

                state = state.copy(
                    fav = state.fav.copy(
                        isSelected = !state.fav.isSelected,
                        totalSongs = if (!temp) state.fav.totalSongs + 1
                        else state.fav.totalSongs - 1
                    )
                )
            }

            EditPlaylistUiEvent.DoneClick -> {
                if (
                    state.playlist.mapNotNull {
                        if (it.isSelected) it.id else null
                    }.isEmpty()
                ) state = state.copy(
                    isNavigateBack = true
                )


                // todo delete
                viewModelScope.launch(Dispatchers.IO) {
                    state = state.copy(
                        isMakingApiCall = true
                    )

                    delay(1500)

                    state = state.copy(
                        isMakingApiCall = false,
                        isNavigateBack = true
                    )
                }

//                if (!state.isInternetAvailable) {
//                    onEvent(EditPlaylistUiEvent.EmitToast("Please check your Internet connection"))
//
//                    return
//                }
//
//                state = state.copy(
//                    isMakingApiCall = true
//                )
//
//                viewModelScope.launch(Dispatchers.IO) {
//                    val playlistId = async {
//                        state.playlist.mapNotNull {
//                            if (it.isSelected) it.id else null
//                        }
//                    }.await()
//
//                    val song = api.addSongToPlaylist(
//                        req = AddSongToPlaylistReq(
//                            songId = state.songId,
//                            isAddToFavourite = state.fav.isSelected,
//                            listOfPlaylistId = playlistId
//                        )
//                    )
//
//                    if (song.id == -1L) {
//                        onEvent(EditPlaylistUiEvent.SomethingWentWrong)
//
//                        state = state.copy(
//                            isMakingApiCall = false
//                        )
//
//                        return@launch
//                    }
//
//                    db.addToPlaylist(
//                        entry = song.toPlaylistSongTable(),
//                        playlistId = playlistId
//                    )
//
//                    state = state.copy(
//                        isMakingApiCall = false,
//                        isNavigateBack = true
//                    )
//                }
            }
        }
    }

    private fun search(query: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = db.searchPlaylist(query).groupBy {
            it.playlistId
        }.map {
            EditPlaylistUiPlaylist(
                id = it.key,
                name = it.value[0].name,
                totalSongs = it.value.size,
                isSelected = it.value.map { song ->
                    if (song.playlistId == state.songId) true else null
                }.firstOrNull() ?: false,
                urls = it.value.map { song ->
                    song.coverImage
                }.take(4)
            )
        }

        state = state.copy(
            playlist = result
        )
    }
}