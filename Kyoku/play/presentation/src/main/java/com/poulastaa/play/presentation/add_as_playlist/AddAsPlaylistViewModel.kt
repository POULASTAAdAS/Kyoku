package com.poulastaa.play.presentation.add_as_playlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.AddPlaylistResType
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.ExploreType
import com.poulastaa.core.domain.add_playlist.AddPlaylistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddAsPlaylistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: AddPlaylistRepository,
) : ViewModel() {
    var state by mutableStateOf(AddAsPlaylistUiState())
        private set

    private val _uiEvent = Channel<AddAsPlaylistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: AddAsPlaylistUiEvent) {
        when (event) {
            is AddAsPlaylistUiEvent.OnNameChange -> {
                state = state.copy(
                    name = event.name
                )
            }

            AddAsPlaylistUiEvent.OnCancel -> viewModelScope.launch {
                _uiEvent.send(AddAsPlaylistUiAction.Cancel)
            }

            AddAsPlaylistUiEvent.OnSubmit -> viewModelScope.launch {
                if (state.isMakingApiCall) return@launch

                state = state.copy(
                    isMakingApiCall = true
                )

                val response = repo.savePlaylist(
                    state.prevSong.map { it.songId },
                    state.name,
                    state.exploreType
                )

                when (response) {
                    is Result.Error -> when (response.error) {
                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                            AddAsPlaylistUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_no_internet
                                )
                            )
                        )

                        else -> _uiEvent.send(
                            AddAsPlaylistUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_something_went_wrong
                                )
                            )
                        )
                    }

                    is Result.Success -> when (response.data) {
                        AddPlaylistResType.NAME_CONFLICT -> _uiEvent.send(
                            AddAsPlaylistUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.playlist_name_conflict
                                )
                            )
                        )

                        AddPlaylistResType.SAVED -> _uiEvent.send(
                            AddAsPlaylistUiAction.Success
                        )

                        AddPlaylistResType.FAILED -> _uiEvent.send(
                            AddAsPlaylistUiAction.EmitToast(
                                UiText.StringResource(
                                    R.string.error_something_went_wrong
                                )
                            )
                        )
                    }
                }

                state = state.copy(
                    isMakingApiCall = false
                )
            }
        }
    }

    fun populate(exploreType: ExploreType) {
        readHeader()
        viewModelScope.launch {
            val playlist = repo.getSongId(exploreType)

            val name = when (exploreType) {
                ExploreType.POPULAR -> "Popular Song Mix"
                ExploreType.OLD_GEM -> "Old Gem"
                ExploreType.ARTIST_MIX -> "Favourite Artist Mix"
            }

            state = state.copy(
                name = "$name ${LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))}",
                prevSong = playlist,
                exploreType = exploreType,
                isLoading = false,
                isMakingApiCall = false
            )
        }
    }

    private fun readHeader() = viewModelScope.launch {
        ds.readTokenOrCookie().collectLatest {
            state = state.copy(header = it)
        }
    }
}