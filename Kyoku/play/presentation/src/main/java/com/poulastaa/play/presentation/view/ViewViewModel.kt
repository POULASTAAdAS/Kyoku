package com.poulastaa.play.presentation.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.view.ViewRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.domain.ViewSongOperation
import com.poulastaa.play.presentation.view.components.ViewDataType
import com.poulastaa.play.presentation.view_artist.ViewArtistOtherScreen
import com.poulastaa.play.presentation.view_artist.ViewArtistUiAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ViewViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: ViewRepository
) : ViewModel() {
    var state by mutableStateOf(ViewUiState())
        private set

    private val _uiEvent = Channel<ViewUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun loadData(
        id: Long,
        type: ViewDataType
    ) {
        viewModelScope.launch {
            when (type) {
                ViewDataType.PLAYLIST -> repo.getPlaylistOnId(id)

                ViewDataType.ALBUM -> repo.getAlbumOnId(id)

                ViewDataType.FEV -> repo.getFev().map { it.toViewData() }

                ViewDataType.ARTIST_MIX -> repo.getArtistMix().map { it.toViewData() }

                ViewDataType.POPULAR_MIX -> repo.getPopularMix().map { it.toViewData() }

                ViewDataType.OLD_MIX -> repo.getOldMix().map { it.toViewData() }
            }.let { result ->
                async {
                    when (result) {
                        is Result.Error -> {
                            state = when (result.error) {
                                DataError.Network.NO_INTERNET -> {
                                    state.copy(
                                        loadingState = DataLoadingState.ERROR
                                    )
                                }

                                else -> {
                                    state.copy(
                                        loadingState = DataLoadingState.ERROR
                                    )
                                }
                            }
                        }

                        is Result.Success -> {
                            withContext(Dispatchers.Main) {
                                when (type) {
                                    ViewDataType.PLAYLIST -> {
                                        state = state.copy(
                                            data = result.data.toViewUiData(),
                                        )
                                    }

                                    ViewDataType.ALBUM -> {
                                        val isSavedAlbum = repo.isSavedAlbum(id)

                                        state = state.copy(
                                            isSavedData = isSavedAlbum,
                                            data = result.data.toViewUiData(),
                                        )
                                    }

                                    ViewDataType.FEV -> {
                                        state = state.copy(
                                            data = result.data.listOfSong.toOtherData().copy(
                                                name = "Favourite"
                                            )
                                        )
                                    }

                                    ViewDataType.ARTIST_MIX -> {
                                        state = state.copy(
                                            data = result.data.listOfSong.toOtherData().copy(
                                                name = "Artist Mix"
                                            )
                                        )
                                    }

                                    ViewDataType.POPULAR_MIX -> {
                                        state = state.copy(
                                            data = result.data.listOfSong.toOtherData().copy(
                                                name = "Popular Song Mix"
                                            )
                                        )
                                    }

                                    ViewDataType.OLD_MIX -> {
                                        state = state.copy(
                                            data = result.data.listOfSong.toOtherData().copy(
                                                name = "Popular Song From Your Time"
                                            )
                                        )
                                    }
                                }

                                state = state.copy(
                                    loadingState = DataLoadingState.LOADED
                                )
                            }
                        }
                    }
                }.await()
            }
        }

        readHeader()
    }

    fun onEvent(event: ViewUiEvent) {
        when (event) {
            ViewUiEvent.OnPlayClick -> {

            }

            ViewUiEvent.OnShuffleClick -> {

            }

            ViewUiEvent.OnDownloadClick -> {

            }

            is ViewUiEvent.OnSongClick -> {

            }

            is ViewUiEvent.OnMoveClick -> {

            }

            is ViewUiEvent.OnThreeDotClick -> {
                viewModelScope.launch {
                    val entry =
                        state.data.listOfSong.find { it.id == event.songId } ?: return@launch

                    val isInFavourite = async { repo.isSongInFavourite(entry.id) }

                    val threeDotOperations = mutableListOf<ViewSongOperation>().apply {
                        if (state.isPlayingQueue) {
                            add(ViewSongOperation.PLAY_NEXT)
                            add(ViewSongOperation.PLAY_LAST)
                        } else add(ViewSongOperation.PLAY)
                        add(ViewSongOperation.ADD_TO_PLAYLIST)
                        if (!isInFavourite.await()) add(ViewSongOperation.ADD_TO_FAVOURITE)
                        add(ViewSongOperation.VIEW_ARTISTS)
                    }

                    state = state.copy(
                        data = state.data.copy(
                            listOfSong = state.data.listOfSong.map { song ->
                                if (song.id == event.songId) song.copy(
                                    isExpanded = !song.isExpanded
                                ) else song
                            }
                        ),
                        threeDotOperations = threeDotOperations
                    )
                }
            }

            is ViewUiEvent.OnThreeDotClose -> {
                state = state.copy(
                    data = state.data.copy(
                        listOfSong = state.data.listOfSong.map { song ->
                            if (song.id == event.songId) song.copy(
                                isExpanded = false
                            ) else song
                        }
                    ),
                    threeDotOperations = emptyList()
                )
            }

            is ViewUiEvent.OnThreeDotItemClick -> {
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
                                ViewUiAction.Navigate(
                                    ViewOtherScreen.ViewArtist(
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
                                                ViewUiAction.EmitToast(
                                                    UiText.StringResource(
                                                        R.string.error_no_internet
                                                    )
                                                )
                                            )
                                        }

                                        else -> {
                                            _uiEvent.send(
                                                ViewUiAction.EmitToast(
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
                                        ViewUiAction.EmitToast(
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
                                ViewUiAction.Navigate(
                                    ViewOtherScreen.ViewArtist(
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

    private fun readHeader() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collectLatest {
                state = state.copy(
                    header = it
                )
            }
        }
    }
}