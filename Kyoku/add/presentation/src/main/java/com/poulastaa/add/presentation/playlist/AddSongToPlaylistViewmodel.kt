package com.poulastaa.add.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.ERROR_LOTTIE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class AddSongToPlaylistViewmodel @Inject constructor(
    private val repo: AddSongToPlaylistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddSongToPlaylistUiState())
    val state = _state.onStart {
        loadSearchJob?.cancel()
        loadStaticDataJob?.cancel()
        loadSearchJob = loadSearchData()
        loadStaticDataJob = loadStaticData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.0.seconds.inWholeMilliseconds),
        initialValue = AddSongToPlaylistUiState()
    )

    private val _uiState = Channel<AddSongToPlaylistUiEvent>()
    val uiEvent = _uiState.receiveAsFlow()

    private val _searchData: MutableStateFlow<PagingData<AddSongToPlaylistUiItem>> =
        MutableStateFlow(PagingData.empty())
    var searchData = _searchData.asStateFlow()
        private set

    fun init(playlistId: PlaylistId) {
        _state.update {
            it.copy(
                playlistId = playlistId
            )
        }
    }

    private var loadStaticDataJob: Job? = null
    private var loadSearchJob: Job? = null

    fun onAction(action: AddSongToPlaylistUiAction) {
        when (action) {
            is AddSongToPlaylistUiAction.OnItemClick -> {
                if (_state.value.isSavingSong || _state.value.playlistId == -1L) return

                when (action.pageType) {
                    AddSongToPlaylistUiAction.PageType.SEARCH -> when (action.type) {
                        AddToPlaylistItemUiType.PLAYLIST -> _state.update {
                            it.copy(
                                playlistScreenState = OtherScreenUiState(
                                    isVisible = true,
                                    otherId = action.itemId
                                )
                            )
                        }

                        AddToPlaylistItemUiType.ALBUM -> _state.update {
                            it.copy(
                                albumScreenState = OtherScreenUiState(
                                    isVisible = true,
                                    otherId = action.itemId
                                )
                            )
                        }

                        AddToPlaylistItemUiType.ARTIST -> _state.update {
                            it.copy(
                                artistScreenState = OtherScreenUiState(
                                    isVisible = true,
                                    otherId = action.itemId
                                ),
                                playlistScreenState = OtherScreenUiState(
                                    isVisible = false,
                                    otherId = -1
                                ),
                                albumScreenState = OtherScreenUiState(
                                    isVisible = false,
                                    otherId = -1
                                )
                            )
                        }

                        AddToPlaylistItemUiType.SONG -> saveSong(_state.value.playlistId, action)
                    }

                    else -> if (action.type == AddToPlaylistItemUiType.SONG && _state.value.playlistId != -1L) saveSong(
                        _state.value.playlistId,
                        action
                    )
                }
            }

            is AddSongToPlaylistUiAction.OnOtherScreenClose -> when (action.type) {
                AddToPlaylistItemUiType.PLAYLIST -> _state.update {
                    it.copy(
                        playlistScreenState = OtherScreenUiState(
                            isVisible = false,
                            otherId = -1
                        )
                    )
                }

                AddToPlaylistItemUiType.ALBUM -> _state.update {
                    it.copy(
                        albumScreenState = OtherScreenUiState(
                            isVisible = false,
                            otherId = -1
                        )
                    )
                }

                AddToPlaylistItemUiType.ARTIST -> _state.update {
                    it.copy(
                        artistScreenState = OtherScreenUiState(
                            isVisible = false,
                            otherId = -1
                        )
                    )
                }

                AddToPlaylistItemUiType.SONG -> return
            }

            is AddSongToPlaylistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = action.value
                    )
                }

                loadSearchJob?.cancel()
                loadSearchJob = loadSearchData()
            }

            is AddSongToPlaylistUiAction.OnSearchFilterTypeChange -> {
                if (_state.value.searchScreenFilterType == action.type) return

                _state.update {
                    it.copy(
                        searchScreenFilterType = action.type
                    )
                }

                loadSearchJob?.cancel()
                loadSearchJob = loadSearchData()
            }
        }
    }

    private fun loadStaticData() = viewModelScope.launch {
        when (val result = repo.loadStaticData()) {
            is Result.Error -> when (result.error) {
                DataError.Network.NO_INTERNET -> _state.update {
                    it.copy(
                        loadingType = LoadingType.Error(
                            type = LoadingType.ERROR_TYPE.NO_INTERNET,
                            lottieId = ERROR_LOTTIE_ID
                        )
                    )
                }

                else -> _state.update {
                    it.copy(
                        loadingType = LoadingType.Error(
                            type = LoadingType.ERROR_TYPE.UNKNOWN,
                            lottieId = ERROR_LOTTIE_ID
                        )
                    )
                }
            }

            is Result.Success -> _state.update {
                it.copy(
                    staticData = result.data.map { it.toAddSongToPlaylistPageUiItem() },
                    loadingType = if (result.data.isNotEmpty()) LoadingType.Content else LoadingType.Error(
                        type = LoadingType.ERROR_TYPE.UNKNOWN,
                        lottieId = ERROR_LOTTIE_ID
                    )
                )
            }
        }
    }

    private fun loadSearchData() = viewModelScope.launch {
        _searchData.update { PagingData.empty() }

        repo.search(
            playlistId = _state.value.playlistId,
            query = _state.value.query.trim(),
            filterType = _state.value.searchScreenFilterType.toDtoDtoAddSongToPlaylistSearchFilterType()
        ).cachedIn(viewModelScope).collectLatest { list ->
            _searchData.update {
                list.map {
                    it.toAddSongToPlaylistUiItem()
                }
            }
        }
    }

    private fun saveSong(
        playlistId: PlaylistId,
        action: AddSongToPlaylistUiAction.OnItemClick,
    ) = viewModelScope.launch {
        _state.update {
            it.copy(
                isSavingSong = true
            )
        }

        when (val result = repo.saveSong(playlistId, action.itemId)) {
            is Result.Error -> when (result.error) {
                DataError.Network.NO_INTERNET -> _uiState.send(
                    AddSongToPlaylistUiEvent.EmitToast(
                        UiText.StringResource(R.string.error_no_internet)
                    )
                )

                else -> _uiState.send(
                    AddSongToPlaylistUiEvent.EmitToast(
                        UiText.StringResource(R.string.error_something_went_wrong)
                    )
                )
            }

            is Result.Success -> _state.update {
                it.copy(
                    staticData = it.staticData.map { item ->
                        if (item.type.toPageType() == action.pageType) item.copy(
                            data = item.data.filterNot { it.id == action.itemId }
                        ) else item
                    }
                )
            }
        }

        _state.update {
            it.copy(
                isSavingSong = false
            )
        }
    }
}