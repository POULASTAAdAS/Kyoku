package com.poulastaa.explore.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.poulastaa.explore.presentation.model.ExploreUiItem
import com.poulastaa.explore.presentation.search.album.ExploreAlbumUiState
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

@HiltViewModel
internal class ExploreAlbumViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ExploreAlbumUiState())
    val state = _state.onStart {
        albumJob?.cancel()
        albumJob = getAlbum()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExploreAlbumUiState()
    )

    private val _uiEvent = Channel<ExploreAlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _album: MutableStateFlow<PagingData<ExploreUiItem>> =
        MutableStateFlow(PagingData.empty())
    var album = _album.asStateFlow()
        private set

    private var albumJob: Job? = null

    fun onAction(action: ExploreAlbumUiAction) {
        when (action) {
            is ExploreAlbumUiAction.OnAlbumClick -> {

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
            PagingData.empty()
        }


    }
}