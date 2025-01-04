package com.poulastaa.setup.presentation.spotify_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ImportPlaylistViewmodel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ImportPlaylistUiState())

    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ImportPlaylistUiState()
        )

    fun onAction(action: ImportPlaylistUiAction) {
        when (action) {
            is ImportPlaylistUiAction.OnLinkChange -> {

            }

            ImportPlaylistUiAction.OnImportClick -> {

            }

            is ImportPlaylistUiAction.OnPlaylistClick -> {
                _state.update { state ->
                    state.copy(
                        data = state.data.map { item ->
                            if (item.playlist.id == action.playlistId) item.copy(
                                isExpanded = !item.isExpanded
                            ) else item
                        }
                    )
                }
            }

            ImportPlaylistUiAction.OnSkipClick -> {

            }
        }
    }
}