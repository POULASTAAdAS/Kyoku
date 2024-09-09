package com.poulastaa.play.presentation.create_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.model.CreatePlaylistType
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.repository.create_playlist.CreatePlaylistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: CreatePlaylistRepository
) : ViewModel() {
    var state by mutableStateOf(CreatePlaylistUiState())
        private set

    private val _uiEvent = Channel<CreatePlaylistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _pagingData: MutableStateFlow<PagingData<CreatePlaylistPagingUiData>> =
        MutableStateFlow(PagingData.empty())
    var pagingData = _pagingData.asStateFlow()
        private set

    private var loadPagingDataJob: Job? = null

    private var generatedData: List<Pair<CreatePlaylistType, List<Song>>> = emptyList()

    fun init(playlistId: Long) {
        state = state.copy(
            playlistId = playlistId
        )
        readHeader()
        loadStaticData()

        loadPagingDataJob?.cancel()
        loadPagingDataJob = loadPagingData()
    }

    fun onEvent(event: CreatePlaylistUiEvent) {
        when (event) {
            is CreatePlaylistUiEvent.OnSongClick -> {
                if (state.savedSongIdList.contains(event.songId)) return
                state = state.copy(savedSongIdList = state.savedSongIdList + event.songId)

                CoroutineScope(Dispatchers.Default).launch {
                    state = state.copy(
                        generatedData = state.generatedData.map { data ->
                            data.copy(
                                list = data.list.mapNotNull { song ->
                                    if (song.id == event.songId) null
                                    else song
                                }
                            )
                        }
                    )

                    _pagingData.value = _pagingData.value.filter {
                        it.id != event.songId
                    }
                }

                if (event.type == CreatePlaylistType.SEARCH) saveSearchedSong(event.songId)
                else saveGeneratedSong(event.songId)
            }

            is CreatePlaylistUiEvent.OnAlbumClick -> {
                state = state.copy(
                    albumUiState = CreatePlaylistExpandedUiState(
                        isExpanded = true,
                        id = event.albumId
                    )
                )
            }

            is CreatePlaylistUiEvent.OnArtistClick -> {
                state = state.copy(
                    artistUiState = CreatePlaylistExpandedUiState(
                        isExpanded = true,
                        id = event.artistId
                    )
                )
            }

            CreatePlaylistUiEvent.OnAlbumCancel -> {
                state = state.copy(
                    albumUiState = CreatePlaylistExpandedUiState()
                )
            }

            CreatePlaylistUiEvent.OnArtistCancel -> {
                state = state.copy(
                    artistUiState = CreatePlaylistExpandedUiState()
                )
            }

            CreatePlaylistUiEvent.OnSearchToggle -> {
                state = state.copy(
                    isSearchEnabled = !state.isSearchEnabled
                )

                if (!state.isSearchEnabled) {
                    state = state.copy(searchQuery = "")

                    loadPagingDataJob?.cancel()
                    loadPagingDataJob = loadPagingData()
                }
            }

            is CreatePlaylistUiEvent.OnFilterTypeChange -> {
                if (state.filterType == event.type) return

                state = state.copy(
                    filterType = event.type
                )

                loadPagingDataJob?.cancel()
                loadPagingDataJob = loadPagingData()
            }

            is CreatePlaylistUiEvent.OnSearchQueryChange -> {
                state = state.copy(
                    searchQuery = event.value
                )

                loadPagingDataJob?.cancel()
                loadPagingDataJob = loadPagingData()
            }
        }
    }

    private fun readHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(header = it)
            }
        }
    }

    private fun loadStaticData() {
        viewModelScope.launch {
            when (val result = repo.getStaticData()) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            _uiEvent.send(
                                CreatePlaylistUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_no_internet)
                                )
                            )
                        }

                        else -> {
                            _uiEvent.send(
                                CreatePlaylistUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }
                }

                is Result.Success -> {
                    val list = result.data.mapNotNull {
                        if (it.second.isNotEmpty()) it.toCreatePlaylistData()
                        else null
                    } + CreatePlaylistData(
                        type = CreatePlaylistType.SEARCH,
                        list = emptyList()
                    )

                    state = state.copy(
                        generatedData = list
                    )

                    generatedData = result.data
                }
            }

            state = if (state.generatedData.isEmpty()) state.copy(
                loadingState = DataLoadingState.ERROR
            ) else state.copy(
                loadingState = DataLoadingState.LOADED
            )
        }
    }

    private fun loadPagingData() = viewModelScope.launch {
        _pagingData.value = PagingData.empty()

        repo.getPagingSong(
            query = state.searchQuery.trim(),
            type = state.filterType,
            savedSongIdList = state.savedSongIdList
        ).cachedIn(viewModelScope).collectLatest { dto ->
            _pagingData.value = dto.map {
                it.toCreatePlaylistPagingUiData()
            }
        }
    }

    private fun saveGeneratedSong(songId: Long) = viewModelScope.launch {
        val song = generatedData.map { it.second }.firstNotNullOfOrNull { list ->
            list.firstOrNull { song -> song.id == songId }
        } ?: return@launch

        val result = repo.saveSong(
            song = song,
            playlistId = state.playlistId
        )

        if (result is Result.Error) {
            when (result.error) {
                DataError.Network.NO_INTERNET -> _uiEvent.send(
                    CreatePlaylistUiAction.EmitToast(
                        UiText.StringResource(R.string.error_no_internet)
                    )
                )

                else -> _uiEvent.send(
                    CreatePlaylistUiAction.EmitToast(
                        UiText.StringResource(R.string.error_something_went_wrong)
                    )
                )
            }
        }
    }

    private fun saveSearchedSong(songId: Long) = viewModelScope.launch {
        val result = repo.saveSong(
            songId = songId,
            playlistId = state.playlistId
        )

        if (result is Result.Error) {
            when (result.error) {
                DataError.Network.NO_INTERNET -> _uiEvent.send(
                    CreatePlaylistUiAction.EmitToast(
                        UiText.StringResource(R.string.error_no_internet)
                    )
                )

                else -> _uiEvent.send(
                    CreatePlaylistUiAction.EmitToast(
                        UiText.StringResource(R.string.error_something_went_wrong)
                    )
                )
            }
        }
    }
}