package com.poulastaa.explore.presentation.search.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.presentation.designsystem.model.LoadingType
import com.poulastaa.explore.domain.repository.album.ExploreAlbumRepository
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.all_from_artist.toExploreUiItem
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

@HiltViewModel
internal class ExploreAlbumViewmodel @Inject constructor(
    private val repo: ExploreAlbumRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ExploreAlbumUiState())
    val state = _state.onStart {
        albumJob?.cancel()
        albumJob = getAlbum()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = ExploreAlbumUiState()
    )

    private val _uiEvent = Channel<ExploreAlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _album: MutableStateFlow<PagingData<ExploreUiItem>> =
        MutableStateFlow(PagingData.Companion.empty())
    var album = _album.asStateFlow()
        private set

    private var albumJob: Job? = null

    fun onAction(action: ExploreAlbumUiAction) {
        when (action) {
            is ExploreAlbumUiAction.OnAlbumClick -> {
                viewModelScope.launch {
                    _uiEvent.send(ExploreAlbumUiEvent.NavigateToAlbum(action.albumId))
                }
            }

            is ExploreAlbumUiAction.OnAlbumThreeDtoClick -> {

            }

            is ExploreAlbumUiAction.OnFilterTypeChange -> {
                if (_state.value.filterType == action.type) return

                _state.update {
                    it.copy(
                        filterType = action.type
                    )
                }

                albumJob?.cancel()
                albumJob = getAlbum()
            }

            is ExploreAlbumUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = action.query
                        )
                    )
                }

                albumJob?.cancel()
                albumJob = getAlbum()
            }

            ExploreAlbumUiAction.OnSearchToggle -> _state.update {
                it.copy(
                    isSearchOpen = it.isSearchOpen.not()
                )
            }
        }
    }

    private fun getAlbum() = viewModelScope.launch {
        _album.update {
            PagingData.Companion.empty()
        }

        repo.getAlbum(
            _state.value.query.value,
            _state.value.filterType.toDtoExploreAlbumFilterType()
        ).cachedIn(viewModelScope).collectLatest { list ->
            _state.update {
                it.copy(
                    loadingType = LoadingType.Content
                )
            }

            _album.update {
                list.map { it.toExploreUiItem() }
            }
        }
    }
}