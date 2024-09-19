package com.poulastaa.play.presentation.song_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.song_artist.SongArtistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongArtistsViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: SongArtistRepository,
) : ViewModel() {
    var state by mutableStateOf(SongArtistsUiState())
        private set

    private var _uiEvent = Channel<SongArtistsUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var readHeaderJob: Job? = null

    fun init(songId: Long) {
        state = state.copy(
            loadingState = DataLoadingState.LOADING,
            artist = emptyList()
        )

        readHeaderJob?.cancel()
        readHeaderJob = readHeader()

        viewModelScope.launch {
            when (val result = repo.getArtistOnSongId(songId)) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                            SongArtistsUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )

                        else -> _uiEvent.send(
                            SongArtistsUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_something_went_wrong
                                )
                            )
                        )
                    }
                }

                is Result.Success -> {
                    state = state.copy(
                        artist = result.data.map { it.toSongArtistUiArtist() }
                    )
                }
            }

            state = state.copy(
                loadingState = DataLoadingState.LOADED
            )
        }
    }

    fun onEvent(event: SongArtistsUiEvent) {
        when (event) {
            is SongArtistsUiEvent.OnArtistClick -> {
                viewModelScope.launch {
                    _uiEvent.send(
                        SongArtistsUiAction.NavigateToArtist(
                            event.artistId
                        )
                    )
                }
            }
        }
    }

    fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(
                header = it
            )
        }
    }
}