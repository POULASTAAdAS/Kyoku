package com.poulastaa.add.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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
        loadStaticDataJob?.cancel()
        loadStaticDataJob = loadStaticData()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.0.seconds.inWholeMilliseconds),
        initialValue = AddSongToPlaylistUiState()
    )

    private val _uiState = Channel<AddSongToPlaylistUiEvent>()
    val uiEvent = _uiState.receiveAsFlow()

    private val _searchData: MutableStateFlow<PagingData<AddToPlaylistUiItem>> =
        MutableStateFlow(
            PagingData.from(
                ((1..5).map {
                    AddToPlaylistUiItem(
                        id = it.toLong(),
                        title = "That Cool Song",
                        type = AddToPlaylistItemUiType.SONG
                    )
                } + (1..5).map {
                    AddToPlaylistUiItem(
                        id = it.toLong(),
                        title = "That Cool Artist",
                        type = AddToPlaylistItemUiType.ARTIST
                    )
                } + (1..5).map {
                    AddToPlaylistUiItem(
                        id = it.toLong(),
                        title = "That Cool Playlist",
                        type = AddToPlaylistItemUiType.PLAYLIST
                    )
                } + (1..5).map {
                    AddToPlaylistUiItem(
                        id = it.toLong(),
                        title = "That Cool Artist",
                        type = AddToPlaylistItemUiType.ALBUM
                    )
                }).shuffled()
            )
        )
    var searchData = _searchData.asStateFlow()
        private set

    private var playlistId: PlaylistId? = null
    fun init(playlistId: PlaylistId) {
        this.playlistId = playlistId
    }

    private var loadStaticDataJob: Job? = null

    fun onAction(action: AddSongToPlaylistUiAction) {
        when (action) {
            is AddSongToPlaylistUiAction.OnItemClick -> {
                if (_state.value.isSavingSong || playlistId == null) return

                if (action.type == AddToPlaylistItemUiType.SONG) viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isSavingSong = true
                        )
                    }

                    when (val result = repo.saveSong(playlistId ?: return@launch, action.itemId)) {
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
                                    if (item.type == action.pageType) item.copy(
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

            is AddSongToPlaylistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = action.value
                    )
                }

                loadStaticDataJob?.cancel()
                loadStaticDataJob = loadStaticData()
            }

            is AddSongToPlaylistUiAction.OnSearchFilterTypeChange -> {
                if (_state.value.searchScreenFilterType == action.type) return

                _state.update {
                    it.copy(
                        searchScreenFilterType = action.type
                    )
                }

                loadStaticDataJob?.cancel()
                loadStaticDataJob = loadStaticData()
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
                    staticData = result.data.map { it.toAddSongToPlaylistPageUiItem() }
                )
            }
        }
    }
}