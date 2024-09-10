package com.poulastaa.play.presentation.create_playlist.album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.create_playlist.album.CreatePlaylistAlbumRepository
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.root_drawer.toUiAlbum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CreatePlaylistAlbumViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: CreatePlaylistAlbumRepository
) : ViewModel() {
    var state by mutableStateOf(CreatePlaylistAlbumUiState())
        private set

    fun init(albumId: Long, savedSongIdList: List<Long>) {

        loadAlbum(albumId, savedSongIdList)
        readHeader()
    }

    fun onEvent(event: CreatePlaylistAlbumUiEvent) {
        when (event) {
            is CreatePlaylistAlbumUiEvent.OnSongClick -> {
                state = state.copy(
                    albumSongs = state.albumSongs.filter { it.id != event.songId }
                )
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

    private fun loadAlbum(albumId: Long, savedSongIdList: List<Long>) {
        viewModelScope.launch {
            state = when (val result = repo.getAlbum(
                albumId = albumId,
                savedSongIdList = savedSongIdList
            )) {
                is Result.Error -> {
                    state.copy(
                        loadingState = DataLoadingState.ERROR
                    )
                }

                is Result.Success -> {
                    state.copy(
                        album = result.data.album.toUiAlbum(),
                        albumSongs = result.data.listOfSong.map { it.toCreatePlaylistUiData() },
                        loadingState = DataLoadingState.LOADED
                    )
                }
            }
        }
    }
}