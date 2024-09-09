package com.poulastaa.play.presentation.create_playlist.artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.create_playlist.artist.CreatePlaylistArtistRepository
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.presentation.create_playlist.CreatePlaylistPagingUiData
import com.poulastaa.play.presentation.create_playlist.toCreatePlaylistPagingUiData
import com.poulastaa.play.presentation.explore_artist.ExploreArtistUiAction
import com.poulastaa.play.presentation.root_drawer.library.toUiArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistArtistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: CreatePlaylistArtistRepository
) : ViewModel() {
    var state by mutableStateOf(CreatePlaylistArtistUiState())
        private set

    private val _uiEvent = Channel<ExploreArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _album: MutableStateFlow<PagingData<CreatePlaylistPagingUiData>> =
        MutableStateFlow(PagingData.empty())
    var album = _album.asStateFlow()
        private set

    private val _song: MutableStateFlow<PagingData<CreatePlaylistPagingUiData>> =
        MutableStateFlow(PagingData.empty())
    var song = _song.asStateFlow()
        private set

    private var savedSongIdList: List<Long> = emptyList()

    init {
        readHeader()
    }

    fun init(artistId: Long) {
        viewModelScope.launch {
            val result = async {
                repo.getArtist(artistId)
            }.await()

            if (result is Result.Success) {
                withContext(Dispatchers.IO) {
                    state = state.copy(
                        artist = result.data.toUiArtist(),
                        loadingState = DataLoadingState.LOADED
                    )
                }
            } else {
                state = state.copy(
                    loadingState = DataLoadingState.ERROR
                )

                return@launch
            }

            loadAlbum()
            loadSong()
        }
    }

    fun updateSavedSongIdList(list: List<Long>) {
        savedSongIdList = list
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

    private fun loadAlbum() = viewModelScope.launch {
        repo.getPagingAlbum(state.artist.id).cachedIn(viewModelScope).collectLatest { pagingData ->
            _album.value = pagingData.map { album ->
                album.toCreatePlaylistPagingUiData()
            }
        }
    }

    private fun loadSong() = viewModelScope.launch {
        repo.getPagingSong(state.artist.id, savedSongIdList).cachedIn(viewModelScope)
            .collectLatest { pagingData ->
                _song.value = pagingData.map { song ->
                    song.toCreatePlaylistPagingUiData()
                }
            }
    }

    fun clear() {
        savedSongIdList = emptyList()
        _album.value = PagingData.empty()
        _song.value = PagingData.empty()
    }
}