package com.poulastaa.explore.presentation.search.artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.poulastaa.explore.presentation.model.ExploreUiItem
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
internal class ExploreArtistViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ExploreArtistUiState())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExploreArtistUiState()
    )

    private val _uiEvent = Channel<ExploreArtistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _artist: MutableStateFlow<PagingData<ExploreUiItem>> =
        MutableStateFlow(PagingData.Companion.empty())
    var artist = _artist.asStateFlow()
        private set

    private var getArtistJob: Job? = null

    fun onAction(action: ExploreArtistUiAction) {
        when (action) {
            is ExploreArtistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = action.message
                        )
                    )
                }

                getArtistJob?.cancel()
                getArtistJob = getArtist()
            }

            ExploreArtistUiAction.OnSearchToggle -> _state.update {
                it.copy(
                    isSearchOpen = it.isSearchOpen.not()
                )
            }

            is ExploreArtistUiAction.OnFilterTypeChange -> {
                if (_state.value.filterType == action.type) return

                _state.update {
                    it.copy(
                        filterType = action.type
                    )
                }

                getArtistJob?.cancel()
                getArtistJob = getArtist()
            }

            is ExploreArtistUiAction.OnArtistClick -> viewModelScope.launch {
                _uiEvent.send(ExploreArtistUiEvent.NavigateToArtist(action.artistId))
            }
        }
    }

    private fun getArtist() = viewModelScope.launch {
        _artist.update {
            PagingData.empty()
        }


    }
}