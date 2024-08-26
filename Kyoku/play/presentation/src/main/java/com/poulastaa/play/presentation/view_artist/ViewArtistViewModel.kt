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
import com.poulastaa.play.domain.DataLoadingState
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
    private val repo: ViewArtistRepository
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
            val resultDef = async { repo.getData(artistId) }
            val isFollowed = async { repo.isArtistAlreadyFollowed(artistId) }

            when (val result = resultDef.await()) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            state = state.copy(
                                isInternetError = true
                            )
                        }

                        else -> Unit
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