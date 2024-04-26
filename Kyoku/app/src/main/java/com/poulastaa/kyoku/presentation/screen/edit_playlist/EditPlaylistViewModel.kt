package com.poulastaa.kyoku.presentation.screen.edit_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.playlist.AddSongToPlaylistReq
import com.poulastaa.kyoku.data.model.UiEvent
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
    private var removeList = ArrayList<Pair<Long, Boolean>>()

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
            async {
                val list = db.getPlaylistIdOnSongId(id)

                list.forEach {
                    removeList.add(Pair(it, false))
                }

                state = state.copy(
                    addList = list
                )
            }.await()

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
                                    isSelected = state.addList.contains(it.key),
                                    urls = it.value.map { song ->
                                        song.coverImage
                                    }.take(4)
                                )
                            }
                        }.await()
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
        }.let {

            state = state.copy(
                loadingStatus = LoadingStatus.NOT_LOADING
            )
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

                searchJob?.cancel()
                restoreAfterSearch()
            }

            is EditPlaylistUiEvent.PlaylistClick -> {
                if (state.isMakingApiCall) return

                state = if (state.addList.contains(event.id)) {
                    val list = ArrayList(state.addList)

                    list.remove(event.id)

                    removeList = removeList.map {
                        if (it.first == event.id) it.copy(
                            second = true
                        )
                        else it
                    } as ArrayList<Pair<Long, Boolean>>

                    state.copy(
                        addList = list.toList()
                    )
                } else {
                    val list = ArrayList<Long>()
                    list.add(event.id)

                    state.copy(
                        addList = list.toList()
                    )
                }

                state = state.copy(
                    playlist = state.playlist.map {
                        if (it.id == event.id) {
                            it.copy(
                                isSelected = state.addList.contains(it.id)
                            )
                        } else it
                    }
                )
            }

            is EditPlaylistUiEvent.FavClick -> {
                if (state.isMakingApiCall) return

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
                if (state.isMakingApiCall) return

                if (
                    state.playlist.mapNotNull {
                        if (it.isSelected) it.id else null
                    }.isEmpty()
                ) state = state.copy(
                    isNavigateBack = true
                )

                if (!state.isInternetAvailable) {
                    onEvent(EditPlaylistUiEvent.EmitToast("Please check your Internet connection"))

                    return
                }

                state = state.copy(
                    isMakingApiCall = true
                )

                viewModelScope.launch(Dispatchers.IO) {
                    val song = api.addSongToPlaylist(
                        req = AddSongToPlaylistReq(
                            songId = state.songId,
                            isAddToFavourite = state.fav.isSelected,
                            add = state.addList,
                            remove = removeList.mapNotNull {
                                if (it.second) it.first else null
                            }
                        )
                    )

                    if (song.id == -1L) {
                        onEvent(EditPlaylistUiEvent.SomethingWentWrong)

                        state = state.copy(
                            isMakingApiCall = false
                        )

                        return@launch
                    }

                    db.editPlaylist(
                        song = song,
                        isFavourite = state.fav.isSelected,
                        addList = state.addList,
                        removeList = removeList.mapNotNull {
                            if (it.second) it.first else null
                        }
                    )

                    state = state.copy(
                        isMakingApiCall = false,
                        isNavigateBack = true
                    )
                }
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
                isSelected = state.addList.contains(it.key),
                urls = it.value.map { song ->
                    song.coverImage
                }.take(4)
            )
        }

        state = state.copy(
            playlist = result
        )
    }

    private fun restoreAfterSearch() {
        searchJob?.cancel()
        search("")
    }
}