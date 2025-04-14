package com.poulastaa.add.presentation.playlist.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.model.AlbumId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddSongToPlaylistAlbumViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(AddSongToPlaylistAlbumUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddSongToPlaylistAlbumUiState()
    )

    private val _uiEvent = Channel<AddSongToPlaylistAlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun init(albumId: AlbumId) = loadAlbum(albumId)

    fun onAction(action: AddSongToPlaylistAlbumUiAction) {
        when (action) {
            is AddSongToPlaylistAlbumUiAction.OnItemClick -> {

            }
        }
    }

    private fun loadAlbum(albumId: AlbumId) {
        viewModelScope.launch {

        }
    }
}