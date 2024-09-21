package com.poulastaa.play.presentation.view_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.view_artist.ViewArtistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.domain.ViewSongOperation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewArtistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: ViewArtistRepository,
) : ViewModel() {
    var state by mutableStateOf(ViewArtistUiState())
        private set

    private val _uiEvent = Channel<ViewArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadHeader()
    }

    fun loadData(artistId: Long) {
        viewModelScope.launch {
            state = state.copy(
                artistId = artistId
            )

            val resultDef = async { repo.getData(artistId) }
            val isFollowed = async { repo.isArtistAlreadyFollowed(artistId) }

            when (val result = resultDef.await()) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            state = state.copy(
                                toast = state.toast.copy(
                                    isError = true,
                                    message = UiText.StringResource(R.string.error_no_internet)
                                )
                            )
                        }

                        else -> {
                            state = state.copy(
                                toast = state.toast.copy(
                                    isError = true,
                                    message = UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }
                }

                is Result.Success -> {
                    state = state.copy(
                        data = result.data.toUiArtistData(isFollowed.await())
                    )
                }
            }

            state = if (state.data.listOfSong.isEmpty() &&
                state.data.artist.name.isEmpty()
            ) state.copy(loadingState = DataLoadingState.ERROR)
            else state.copy(loadingState = DataLoadingState.LOADED)
        }
    }

    fun onEvent(event: ViewArtistUiEvent) {
        when (event) {
            ViewArtistUiEvent.FollowArtistToggleClick -> {
                viewModelScope.launch {
                    val followStatus = state.data.isArtistFollowed

                    val result =
                        if (followStatus) repo.unFollowArtist(state.artistId)
                        else repo.followArtist(state.artistId)

                    when (result) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> {
                                    state = state.copy(
                                        toast = state.toast.copy(
                                            isError = true,
                                            message = UiText.StringResource(R.string.error_no_internet)
                                        )
                                    )
                                }

                                else -> {
                                    state = state.copy(
                                        toast = state.toast.copy(
                                            isError = true,
                                            message = UiText.StringResource(R.string.error_something_went_wrong)
                                        )
                                    )
                                }
                            }
                        }

                        is Result.Success -> {
                            state = if (followStatus) state.copy(
                                data = state.data.copy(
                                    isArtistFollowed = false,
                                    popularity = state.data.popularity.dec()
                                ),
                                toast = state.toast.copy(
                                    isError = false,
                                    message = UiText.DynamicString("You unfollowed ${state.data.artist.name}")
                                )
                            )
                            else state.copy(
                                data = state.data.copy(
                                    isArtistFollowed = true,
                                    popularity = state.data.popularity.inc()
                                ),
                                toast = state.toast.copy(
                                    isError = false,
                                    message = UiText.DynamicString("You followed ${state.data.artist.name}")
                                )
                            )
                        }
                    }
                }
            }

            is ViewArtistUiEvent.OnSongClick -> {

            }

            is ViewArtistUiEvent.ThreeDotEvent -> {
                when (event) {
                    is ViewArtistUiEvent.ThreeDotEvent.OnClick -> {
                        viewModelScope.launch {
                            val entry =
                                state.data.listOfSong.find { it.id == event.id } ?: return@launch

                            val isInFavourite = async { repo.isSongInFavourite(entry.id) }

                            val threeDotOperations = mutableListOf<ViewSongOperation>().apply {
                                if (state.isPlayingQueue) {
                                    add(ViewSongOperation.PLAY_NEXT)
                                    add(ViewSongOperation.PLAY_LAST)
                                } else add(ViewSongOperation.PLAY)
                                add(ViewSongOperation.ADD_TO_PLAYLIST)
                                if (!isInFavourite.await()) add(ViewSongOperation.ADD_TO_FAVOURITE)
                            }

                            state = state.copy(
                                data = state.data.copy(
                                    listOfSong = state.data.listOfSong.map { song ->
                                        if (song.id == event.id) song.copy(
                                            isExpanded = !song.isExpanded
                                        ) else song
                                    }
                                ),
                                threeDotOperations = threeDotOperations
                            )
                        }
                    }

                    is ViewArtistUiEvent.ThreeDotEvent.OnCloseClick -> {
                        state = state.copy(
                            data = state.data.copy(
                                listOfSong = state.data.listOfSong.map { song ->
                                    if (song.id == event.id) song.copy(
                                        isExpanded = false
                                    ) else song
                                }
                            ),
                            threeDotOperations = emptyList()
                        )
                    }

                    is ViewArtistUiEvent.ThreeDotEvent.OnThreeDotItemClick -> {
                        state = state.copy(
                            data = state.data.copy(
                                listOfSong = state.data.listOfSong.map { song ->
                                    if (song.id == event.id) song.copy(
                                        isExpanded = false
                                    ) else song
                                }
                            ),
                            threeDotOperations = emptyList()
                        )

                        when (event.operation) {
                            ViewSongOperation.PLAY -> {

                            }

                            ViewSongOperation.PLAY_NEXT -> {

                            }

                            ViewSongOperation.PLAY_LAST -> {

                            }

                            ViewSongOperation.ADD_TO_PLAYLIST -> {


                                viewModelScope.launch {
                                    _uiEvent.send(
                                        ViewArtistUiAction.Navigate(
                                            ViewArtistOtherScreen.AddSongToPlaylist(
                                                id = event.id
                                            )
                                        )
                                    )
                                }
                            }

                            ViewSongOperation.ADD_TO_FAVOURITE -> {
                                viewModelScope.launch {
                                    when (val result = repo.addSongToFavourite(event.id)) {
                                        is Result.Error -> {
                                            when (result.error) {
                                                DataError.Network.NO_INTERNET -> {
                                                    _uiEvent.send(
                                                        ViewArtistUiAction.EmitToast(
                                                            UiText.StringResource(
                                                                R.string.error_no_internet
                                                            )
                                                        )
                                                    )
                                                }

                                                else -> {
                                                    _uiEvent.send(
                                                        ViewArtistUiAction.EmitToast(
                                                            UiText.StringResource(
                                                                R.string.error_something_went_wrong
                                                            )
                                                        )
                                                    )
                                                }
                                            }
                                        }

                                        is Result.Success -> {
                                            _uiEvent.send(
                                                ViewArtistUiAction.EmitToast(
                                                    UiText.StringResource(
                                                        R.string.add_to_favourite
                                                    )
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            ViewSongOperation.VIEW_ARTISTS -> {
                                viewModelScope.launch {
                                    _uiEvent.send(
                                        ViewArtistUiAction.Navigate(
                                            ViewArtistOtherScreen.ViewArtist(
                                                id = event.id
                                            )
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }
}