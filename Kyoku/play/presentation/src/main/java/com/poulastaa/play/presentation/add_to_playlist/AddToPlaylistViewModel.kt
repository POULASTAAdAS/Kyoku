package com.poulastaa.play.presentation.add_to_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.add_to_playlist.AddToPlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToPlaylistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: AddToPlaylistRepository
) : ViewModel() {
    var state by mutableStateOf(AddToPlaylistUiState())
        private set

    init {
        readHeader()
    }

    fun loadData(songId: Long) {
        if (songId == -1L) {
            viewModelScope.launch {

            }

            return
        }

        state = state.copy(
            songId = songId
        )

        loadFavourite(songId)
        loadPlaylist(songId)
    }

    fun onEvent(event: AddToPlaylistUiEvent) {
        when (event) {
            AddToPlaylistUiEvent.EnableSearch -> {
                state = state.copy(
                    isSearchEnable = true,
                    oldPlaylistData = state.playlistData
                )
            }

            AddToPlaylistUiEvent.CancelSearch -> {
                state = state.copy(
                    query = "",
                    isSearchEnable = false,
                    playlistData = state.oldPlaylistData
                )
            }

            is AddToPlaylistUiEvent.OnSearchQueryChange -> {
                val query = event.query

                state = state.copy(
                    query = event.query
                )

                state = if (state.query.isNotBlank()) state.copy(
                    playlistData = state.oldPlaylistData.mapNotNull {
                        if (it.playlist.name.contains(query, ignoreCase = true)) it else null
                    }
                ) else state.copy(
                    playlistData = state.oldPlaylistData
                )
            }

            AddToPlaylistUiEvent.OnFevToggle -> {
                val new = state.favouriteData.selectStatus.new.not()
                val old = state.favouriteData.selectStatus.old

                state = state.copy(
                    favouriteData = state.favouriteData.copy(
                        selectStatus = state.favouriteData.selectStatus.copy(
                            new = new
                        ),
                        totalSongs = if (old) {
                            if (!new) state.favouriteData.totalSongs.dec()
                            else state.favouriteData.totalSongs.inc()
                        } else {
                            if (new) state.favouriteData.totalSongs.inc()
                            else state.favouriteData.totalSongs.dec()
                        }
                    )
                )
            }

            is AddToPlaylistUiEvent.OnPlaylistClick -> {
                val playlist =
                    state.playlistData.first { it.playlist.id == event.playlistId }
                val oldData =
                    state.oldPlaylistData.first { it.playlist.id == event.playlistId }

                viewModelScope.launch(Dispatchers.Default) {
                    val old = async {
                        state.oldPlaylistData.map {
                            if (it.playlist.id == event.playlistId) {
                                it.copy(
                                    selectStatus = toggleSelectStatus(it.selectStatus),
                                    totalSongs = adjustTotalSongs(
                                        it.totalSongs,
                                        oldData.selectStatus
                                    )
                                )
                            } else it
                        }
                    }

                    val new = async {
                        state.playlistData.map {
                            if (it.playlist.id == event.playlistId) {
                                it.copy(
                                    selectStatus = toggleSelectStatus(it.selectStatus),
                                    totalSongs = adjustTotalSongs(
                                        it.totalSongs,
                                        playlist.selectStatus
                                    )
                                )
                            } else it
                        }
                    }


                    state = state.copy(
                        oldPlaylistData = old.await(),
                        playlistData = new.await()
                    )
                }
            }

            AddToPlaylistUiEvent.OnSaveClick -> {
                if (state.isMakingApiCall) return

                state = state.copy(
                    isMakingApiCall = true
                )

                viewModelScope.launch(Dispatchers.IO) {

                }
            }
        }
    }

    private fun readHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }

    private fun loadFavourite(songId: Long) {
        viewModelScope.launch {
            val statusDef = async { repo.checkIfSongInFev(songId) }
            val totalDef = async { repo.getTotalSongsInFev() }

            val status = statusDef.await()
            val total = totalDef.await()

            state = state.copy(
                favouriteData = state.favouriteData.copy(
                    selectStatus = state.favouriteData.selectStatus.copy(
                        old = status,
                        new = status
                    ),
                    totalSongs = total
                )
            )
        }
    }

    private fun loadPlaylist(songId: Long) = viewModelScope.launch {
        val data = repo.getPlaylistData(songId).map { it.toPlaylistData() }

        state = state.copy(
            playlistData = data,
            oldPlaylistData = data
        )
    }

    private fun toggleSelectStatus(
        currentStatus: UiSelectStatus
    ): UiSelectStatus = currentStatus.copy(new = currentStatus.new.not())

    private fun adjustTotalSongs(
        currentTotal: Int,
        selectStatus: UiSelectStatus
    ) = if (selectStatus.old) {
        if (selectStatus.new) currentTotal.dec() else currentTotal.inc()
    } else {
        if (!selectStatus.new) currentTotal.inc() else currentTotal.dec()
    }
}