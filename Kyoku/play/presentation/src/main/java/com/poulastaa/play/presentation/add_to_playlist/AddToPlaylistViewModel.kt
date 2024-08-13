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
        loadFavourite(songId)
        loadPlaylist(songId)
    }

    fun onEvent(event: AddToPlaylistUiEvent) {
        when (event) {
            AddToPlaylistUiEvent.EnableSearch -> {
                state = state.copy(
                    isSearchEnable = true
                )
            }

            AddToPlaylistUiEvent.CancelSearch -> {
                state = state.copy(
                    isSearchEnable = false
                )
            }

            AddToPlaylistUiEvent.OnFevToggle -> {
                val new = state.favouriteData.selectStatus.new.not()

                state = state.copy(
                    favouriteData = state.favouriteData.copy(
                        selectStatus = state.favouriteData.selectStatus.copy(
                            new = new
                        ),
                        totalSongs = if (state.favouriteData.selectStatus.old != new) state.favouriteData.totalSongs.inc()
                        else state.favouriteData.totalSongs.dec()
                    )
                )
            }

            is AddToPlaylistUiEvent.OnPlaylistClick -> {
                val new =
                    state.playlistData.firstOrNull { it.playlist.id == event.playlistId }?.selectStatus?.new?.not()
                        ?: return

                state = state.copy(
                    playlistData = state.playlistData.map {
                        if (it.playlist.id == event.playlistId) {
                            it.copy(
                                selectStatus = it.selectStatus.copy(
                                    new = new
                                ),
                                totalSongs = if (it.selectStatus.old != new) it.totalSongs.inc() else it.totalSongs.dec()
                            )
                        } else it
                    }
                )
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

    private fun loadPlaylist(songId: Long) {
        viewModelScope.launch {

        }
    }
}