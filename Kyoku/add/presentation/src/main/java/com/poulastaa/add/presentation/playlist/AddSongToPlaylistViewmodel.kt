package com.poulastaa.add.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.core.presentation.designsystem.ui.ERROR_LOTTIE_ID
import dagger.hilt.android.lifecycle.HiltViewModel
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
        loadStaticData()
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

    fun onAction(action: AddSongToPlaylistUiAction) {
        when (action) {
            is AddSongToPlaylistUiAction.OnItemClick -> {

            }

            is AddSongToPlaylistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = action.value
                    )
                }
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