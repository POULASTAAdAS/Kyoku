package com.poulastaa.explore.presentation.search.all_from_artist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.poulastaa.core.domain.model.AlbumId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
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

    fun init(albumId: AlbumId) {
        viewModelScope.launch {

        }
    }
}