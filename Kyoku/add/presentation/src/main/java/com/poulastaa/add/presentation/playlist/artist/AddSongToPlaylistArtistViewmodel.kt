package com.poulastaa.add.presentation.playlist.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.presentation.playlist.AddSongToPlaylistUiItem
import com.poulastaa.add.presentation.playlist.AddToPlaylistItemUiType
import com.poulastaa.add.presentation.playlist.toAddSongToPlaylistUiItem
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.toUiPrevArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddSongToPlaylistArtistViewmodel @Inject constructor(
    private val repo: AddSongToPlaylistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddSongToPlaylistArtistUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = AddSongToPlaylistArtistUiState()
    )

    fun init(playlistId: PlaylistId, artistId: ArtistId) {
        if (artistId == -1L) return

        _state.update {
            it.copy(
                playlistId = playlistId,
                loadingType = LoadingType.Loading
            )
        }

        loadArtist(artistId)
        loadDataJob?.cancel()
        loadDataJob = loadData(artistId)
    }

    private var loadDataJob: Job? = null
    private val _uiEvent = Channel<AddSongTOPlaylistArtistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _data: MutableStateFlow<PagingData<AddSongToPlaylistUiItem>> =
        MutableStateFlow(PagingData.empty())
    val data = _data.asStateFlow()

    fun onAction(action: AddSongToPlaylistArtistUiAction) {
        when (action) {
            is AddSongToPlaylistArtistUiAction.OnItemClick -> {
                when (action.type) {
                    AddToPlaylistItemUiType.ALBUM -> if (_state.value.playlistId != -1L) viewModelScope.launch {
                        _uiEvent.send(
                            AddSongTOPlaylistArtistUiEvent.NavigateToAlbum(action.itemId)
                        )
                    }

                    AddToPlaylistItemUiType.SONG -> {
                        if (_state.value.playlistId != -1L) viewModelScope.launch {
                            when (val result =
                                repo.saveSong(_state.value.playlistId, action.itemId)) {
                                is Result.Error -> when (result.error) {
                                    DataError.Network.NO_INTERNET -> _uiEvent.send(
                                        AddSongTOPlaylistArtistUiEvent.EmitToast(
                                            UiText.StringResource(R.string.error_no_internet)
                                        )
                                    )

                                    else -> _uiEvent.send(
                                        AddSongTOPlaylistArtistUiEvent.EmitToast(
                                            UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )
                                }

                                is Result.Success -> _data.update {
                                    it.filter { it.id != action.itemId }
                                }
                            }
                        }
                    }

                    else -> return
                }
            }

            is AddSongToPlaylistArtistUiAction.OnSearchFilterTypeChange -> {
                if (action.type == _state.value.filterType) return

                _state.update {
                    it.copy(
                        filterType = action.type
                    )
                }

                loadDataJob?.cancel()
                loadDataJob = loadData(_state.value.artist.id)
            }

            is AddSongToPlaylistArtistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = action.value
                        )
                    )
                }

                loadDataJob?.cancel()
                loadDataJob = loadData(_state.value.artist.id)
            }
        }
    }

    private fun loadArtist(artistId: ArtistId) = viewModelScope.launch {
        when (val result = repo.loadArtist(artistId)) {
            is Result.Error -> when (result.error) {
                DataError.Network.NO_INTERNET -> {
                    _state.update {
                        it.copy(
                            loadingType = LoadingType.Error(LoadingType.ERROR_TYPE.UNKNOWN)
                        )
                    }

                    _uiEvent.send(
                        AddSongTOPlaylistArtistUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.error_no_internet
                            )
                        )
                    )
                }

                else -> {
                    _state.update {
                        it.copy(
                            loadingType = LoadingType.Error(LoadingType.ERROR_TYPE.UNKNOWN)
                        )
                    }

                    _uiEvent.send(
                        AddSongTOPlaylistArtistUiEvent.EmitToast(
                            UiText.StringResource(
                                R.string.error_no_internet
                            )
                        )
                    )
                }
            }

            is Result.Success -> _state.update {
                it.copy(
                    artist = result.data.toUiPrevArtist(),
                    loadingType = if (result.data.id != -1L) LoadingType.Content
                    else LoadingType.Error(LoadingType.ERROR_TYPE.UNKNOWN)
                )
            }
        }
    }

    private fun loadData(artistId: ArtistId) = viewModelScope.launch {
        _data.update { PagingData.empty() }

        repo.loadArtistSearch(
            playlistId = _state.value.playlistId,
            artistId = artistId,
            query = _state.value.query.value.trim(),
            filterType = _state.value.filterType.toDtoAddSongToPlaylistArtistSearchFilterType()
        ).cachedIn(viewModelScope).collectLatest { data ->
            _data.update {
                data.map { it.toAddSongToPlaylistUiItem() }
            }
        }
    }
}