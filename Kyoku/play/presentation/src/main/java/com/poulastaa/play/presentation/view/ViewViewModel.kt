package com.poulastaa.play.presentation.view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.core.domain.view.ViewRepository
import com.poulastaa.play.presentation.view.components.ViewDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
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
                                        loadingState = ViewLoadingState.ERROR
                                    )
                                }

                                else -> {
                                    state.copy(
                                        loadingState = ViewLoadingState.ERROR
                                    )
                                }
                            }
                        }

                        is Result.Success -> {
                            withContext(Dispatchers.Main) {
                                when (type) {
                                    ViewDataType.PLAYLIST,
                                    ViewDataType.ALBUM -> {
                                        state = state.copy(
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

                                state =state.copy(
                                    loadingState = ViewLoadingState.LOADED
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