package com.poulastaa.play.presentation.create_playlist

import androidx.collection.mutableIntListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

    private val savedSongIdList = mutableIntListOf()

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

            }

            CreatePlaylistUiEvent.OnSearchToggle -> {
                state = state.copy(
                    isSearchEnabled = !state.isSearchEnabled
                )

                if (!state.isSearchEnabled) {
                    state = state.copy(
                        searchQuery = ""
                    )

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

        repo.getPagingSong( // todo send saved songId list
            query = state.searchQuery.trim(),
            type = state.filterType
        ).cachedIn(viewModelScope).collectLatest { dto ->
            _pagingData.value = dto.map {
                it.toCreatePlaylistPagingUiData()
            }
        }
    }
}