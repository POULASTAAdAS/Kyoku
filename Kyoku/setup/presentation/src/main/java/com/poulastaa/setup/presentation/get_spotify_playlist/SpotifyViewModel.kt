package com.poulastaa.setup.presentation.get_spotify_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.get_spotify_playlist.SpotifyRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.BitmapConverter
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.setup.presentation.get_spotify_playlist.mapper.toSpotifyPlaylistId
import com.poulastaa.setup.presentation.get_spotify_playlist.mapper.validateSpotifyLink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpotifyViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repository: SpotifyRepository,
) : ViewModel() {
    var state by mutableStateOf(SpotifyPlaylistUiState())
        private set

    private val _uiEvent = Channel<SpotifyUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    authHeader = it
                )
            }
        }
    }

    fun onEvent(event: SpotifyPlaylistUiEvent) {
        when (event) {
            is SpotifyPlaylistUiEvent.OnLinkChange -> {
                state = state.copy(
                    link = state.link.copy(
                        data = event.data,
                        isErr = false,
                        errText = UiText.DynamicString("")
                    )
                )
            }

            SpotifyPlaylistUiEvent.OnLikeSubmit -> {
                if (!state.link.data.validateSpotifyLink()) {
                    state = state.copy(
                        link = state.link.copy(
                            isErr = true,
                            errText = UiText.StringResource(R.string.error_invalid_spotify_link)
                        )
                    )

                    return
                }

                state = state.copy(
                    isMakingApiCall = true
                )

                viewModelScope.launch(Dispatchers.IO) {
                    when (val result =
                        repository.insertPlaylist(state.link.data.toSpotifyPlaylistId())) {
                        is Result.Error -> {
                            if (result.error is DataError.Network) {
                                when (result.error as DataError.Network) {
                                    DataError.Network.NO_INTERNET -> {
                                        _uiEvent.send(
                                            SpotifyUiAction.EmitToast(UiText.StringResource(R.string.error_no_internet))
                                        )
                                    }

                                    else -> {
                                        _uiEvent.send(
                                            SpotifyUiAction.EmitToast(UiText.StringResource(R.string.error_something_went_wrong))
                                        )
                                    }
                                }
                            }
                        }

                        is Result.Success -> {
                            _uiEvent.send(
                                SpotifyUiAction.EmitToast(UiText.StringResource(R.string.playlist_added))
                            )

                            state = state.copy(
                                link = state.link.copy(
                                    data = ""
                                )
                            )
                        }
                    }

                    state = state.copy(
                        isMakingApiCall = false
                    )
                }
            }

            is SpotifyPlaylistUiEvent.OnPlaylistClick -> {
                state = state.copy(
                    playlists = state.playlists.map {
                        if (it.id == event.id) {
                            it.copy(
                                isExpanded = !it.isExpanded
                            )
                        } else it
                    }
                )
            }

            is SpotifyPlaylistUiEvent.UpdateCoverImage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.updateCoverImage(
                        songId = event.id,
                        encodedString = BitmapConverter.encodeToSting(event.bitmap)
                    )
                }
            }

            SpotifyPlaylistUiEvent.OnSkipClick -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(SpotifyUiAction.NavigateToSetBDate)
                }
            }
        }
    }
}