package com.poulastaa.explore.presentation.search.all_from_artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.explore.domain.model.ExploreAllowedNavigationScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
internal class AllFromArtistViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AllFromArtistUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = AllFromArtistUiState()
    )

    private val _song: MutableStateFlow<PagingData<AllFromArtistUiItem>> =
        MutableStateFlow(PagingData.empty())
    var song = _song.asStateFlow()
        private set

    var _album: MutableStateFlow<PagingData<AllFromArtistUiItem>> =
        MutableStateFlow(PagingData.empty())
    var album = _album.asStateFlow()
        private set

    private val _uiEvent = Channel<AllFromArtistUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(albumId: AlbumId) {
        viewModelScope.launch {

        }
    }

    fun onAction(action: AllFromArtistUiAction) {
        when (action) {
            is AllFromArtistUiAction.OnAlbumClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        AllFromArtistUiEvent.Navigate(
                            ExploreAllowedNavigationScreen.Album(action.albumId)
                        )
                    )
                }
            }

            is AllFromArtistUiAction.OnFilterChange -> {
                if (action.filterType == state.value.filterType) return

                _state.update {
                    it.copy(
                        filterType = action.filterType
                    )
                }
            }

            is AllFromArtistUiAction.OnSearchQueryChange -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = action.query
                        )
                    )
                }

                // perform search
            }

            AllFromArtistUiAction.OnSearchQueryClear -> {
                _state.update {
                    it.copy(
                        query = it.query.copy(
                            value = ""
                        )
                    )
                }
            }

            is AllFromArtistUiAction.OnSongClick -> {
                // todo add to playing queue
            }

            AllFromArtistUiAction.OnToggleSearch -> {
                _state.update {
                    it.copy(
                        isSearchOpen = it.isSearchOpen.not()
                    )
                }
            }
        }
    }
}