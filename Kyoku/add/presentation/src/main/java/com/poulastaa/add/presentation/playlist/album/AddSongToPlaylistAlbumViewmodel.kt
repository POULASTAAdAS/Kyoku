package com.poulastaa.add.presentation.playlist.album

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.presentation.playlist.toAddSongToPlaylistUiItem
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AddSongToPlaylistAlbumViewmodel @Inject constructor(
    private val repo: AddSongToPlaylistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddSongToPlaylistAlbumUiState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddSongToPlaylistAlbumUiState()
    )

    private val _uiEvent = Channel<AddSongToPlaylistAlbumUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var playlistId: PlaylistId? = null

    fun init(albumId: AlbumId, playlistId: PlaylistId) = {
        loadAlbum(albumId)
        this.playlistId = playlistId
    }

    fun onAction(action: AddSongToPlaylistAlbumUiAction) {
        when (action) {
            is AddSongToPlaylistAlbumUiAction.OnItemClick -> {
                playlistId?.let {
                    viewModelScope.launch {
                        val result = repo.saveSong(it, action.songId)

                        when (result) {
                            is Result.Error -> when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    AddSongToPlaylistAlbumUiEvent.EmitToast(
                                        UiText.StringResource(R.string.error_no_internet)
                                    )
                                )

                                else -> _uiEvent.send(
                                    AddSongToPlaylistAlbumUiEvent.EmitToast(
                                        UiText.StringResource(R.string.error_something_went_wrong)
                                    )
                                )
                            }

                            is Result.Success -> _state.update {
                                it.copy(
                                    data = it.data.filterNot { it.id == action.songId }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadAlbum(albumId: AlbumId) {
        playlistId?.let { playlistId ->
            viewModelScope.launch {
                val result = repo.loadAlbum(playlistId,albumId)

                when (result) {
                    is Result.Error -> when (result.error) {
                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                            AddSongToPlaylistAlbumUiEvent.EmitToast(
                                UiText.StringResource(R.string.error_no_internet)
                            )
                        )

                        else -> _uiEvent.send(
                            AddSongToPlaylistAlbumUiEvent.EmitToast(
                                UiText.StringResource(R.string.error_something_went_wrong)
                            )
                        )
                    }

                    is Result.Success -> _state.update {
                        it.copy(
                            data = result.data.map {
                                it.toAddSongToPlaylistUiItem()
                            }
                        )
                    }
                }
            }
        }
    }
}