package com.poulastaa.play.presentation.explore_artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.explore_artist.ExploreArtistRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.core.presentation.ui.model.UiArtist
import com.poulastaa.play.presentation.root_drawer.library.toUiArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreArtistViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val repo: ExploreArtistRepository,
) : ViewModel() {
    var state by mutableStateOf(ExploreArtistUiState())
        private set

    private val _uiEvent = Channel<ExploreArtistUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _song: MutableStateFlow<PagingData<ExploreArtistSingleUiData>> =
        MutableStateFlow(PagingData.empty())
    var song = _song.asStateFlow()
        private set

    private val _album: MutableStateFlow<PagingData<ExploreArtistSingleUiData>> =
        MutableStateFlow(PagingData.empty())
    var album = _album.asStateFlow()
        private set

    init {
        readHeader()
    }

    fun loadData(artistId: Long) {
        if (state.artist.id != artistId) {
            state = state.copy(
                artist = UiArtist(),
            )

            _song.value = PagingData.empty()
            _album.value = PagingData.empty()
        }

        viewModelScope.launch {
            val resultDef = async { repo.getArtist(artistId) }

            when (val result = resultDef.await()) {
                is Result.Error -> {
                    when (result.error) {
                        DataError.Network.NO_INTERNET -> {
                            _uiEvent.send(
                                ExploreArtistUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_no_internet)
                                )
                            )
                        }

                        else -> {
                            _uiEvent.send(
                                ExploreArtistUiAction.EmitToast(
                                    UiText.StringResource(R.string.error_something_went_wrong)
                                )
                            )
                        }
                    }
                }

                is Result.Success -> {
                    state = state.copy(
                        artist = result.data.toUiArtist()
                    )
                }
            }
        }

        viewModelScope.launch { // get paging album
            repo.getArtistSong(artistId).cachedIn(viewModelScope).collectLatest {
                _song.value = it.map { data ->
                    data.toExploreArtistSingleUiData()
                }
            }
        }

        viewModelScope.launch {  // get paging song
            repo.getArtistAlbum(artistId).cachedIn(viewModelScope).collectLatest {
                _album.value = it.map { data ->
                    data.toExploreArtistSingleUiData()
                }
            }
        }
    }

    fun onEvent(event: ExploreArtistUiEvent) {
        when (event) {
            is ExploreArtistUiEvent.OnAlbumClick -> {

            }

            is ExploreArtistUiEvent.OnAlbumThreeDotClick -> {

            }

            is ExploreArtistUiEvent.OnSongClick -> {

            }

            is ExploreArtistUiEvent.OnSongThreeDotClick -> {

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