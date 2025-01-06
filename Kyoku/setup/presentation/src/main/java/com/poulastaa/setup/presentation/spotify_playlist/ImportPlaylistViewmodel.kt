package com.poulastaa.setup.presentation.spotify_playlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.setup.domain.repository.import_playlist.ImportPlaylistRepository
import com.poulastaa.setup.domain.repository.import_playlist.SpotifyPlaylistLinkValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImportPlaylistViewmodel @Inject constructor(
    private val ds: DatastoreRepository,
    private val validator: SpotifyPlaylistLinkValidator,
    private val repo: ImportPlaylistRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ImportPlaylistUiState())

    val state = _state
        .onStart {
            readCookie()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ImportPlaylistUiState()
        )

    private val _uiState = Channel<ImportPlaylistUiEvent>()
    val uiState = _uiState.receiveAsFlow()

    fun onAction(action: ImportPlaylistUiAction) {
        when (action) {
            is ImportPlaylistUiAction.OnLinkChange -> {
                _state.update {
                    it.copy(
                        link = it.link.copy(
                            value = action.link,
                            isErr = false,
                            errText = UiText.DynamicString("")
                        )
                    )
                }
            }

            ImportPlaylistUiAction.OnImportClick -> {
                if (_state.value.isMakingApiCall) return
                val playlistId = validator.validate(_state.value.link.value)
                    ?: return _state.update {
                        it.copy(
                            link = it.link.copy(
                                isErr = true,
                                errText = UiText.StringResource(R.string.error_invalid_spotify_playlist_link)
                            )
                        )
                    }

                _state.update {
                    it.copy(
                        isMakingApiCall = true
                    )
                }


                viewModelScope.launch {
                    val result = repo.importPlaylist(playlistId)

                    Log.d("result", result.toString())

                    _state.update {
                        it.copy(
                            isMakingApiCall = false
                        )
                    }
                }
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
                if (_state.value.isMakingApiCall) return

                viewModelScope.launch {
                    _uiState.send(ImportPlaylistUiEvent.NavigateToBDate)
                }
            }
        }
    }

    private suspend fun readCookie() {
        ds.readTokenOrCookie().collectLatest { cookie ->
            _state.update {
                it.copy(
                    header = cookie
                )
            }
        }
    }
}