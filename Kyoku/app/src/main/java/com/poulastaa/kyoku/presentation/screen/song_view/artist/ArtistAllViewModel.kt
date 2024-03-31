package com.poulastaa.kyoku.presentation.screen.song_view.artist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistAlbum
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiState
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.usecase.ArtistAlbumPagingSource
import com.poulastaa.kyoku.domain.usecase.ArtistSongPagingSource
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistAllViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val albumPager: ArtistAlbumPagingSource,
    private val songPager: ArtistSongPagingSource,
    private val ds: DataStoreOperation
) : ViewModel() {
    private val network = mutableStateOf(NetworkObserver.STATUS.UNAVAILABLE)

    init {
        viewModelScope.launch {
            connectivity.observe().collect {
                network.value = it
                state = state.copy(
                    isInternetAvailable = it == NetworkObserver.STATUS.AVAILABLE,
                    isInternetError = false
                )
                if (!state.isInternetAvailable)
                    state = state.copy(
                        isInternetError = true
                    )
            }
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(ArtistAllUiState())
        private set


    private val _albums: MutableStateFlow<PagingData<ArtistAlbum>> =
        MutableStateFlow(PagingData.empty())
    var albums = _albums.asStateFlow()
        private set

    private val _songs: MutableStateFlow<PagingData<SongPreview>> =
        MutableStateFlow(PagingData.empty())
    var songs = _songs.asStateFlow()
        private set


    init {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                state = state.copy(
                    headerValue = it
                )
            }
        }

        viewModelScope.launch {
            state = state.copy(
                isCooke = when (ds.readAuthType().first()) {
                    AuthType.SESSION_AUTH.name -> true
                    AuthType.JWT_AUTH.name -> false
                    else -> {
                        false
                    }
                }
            )
        }
    }

    fun loadAlbum(name: String) {
        viewModelScope.launch {
            albumPager.load(name)

            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                initialKey = 1
            ) {
                albumPager
            }.flow.cachedIn(viewModelScope).collect {
                _albums.value = it
            }
        }
    }

    fun loadSong(name: String) {
        viewModelScope.launch {
            songPager.load(name)


            Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                initialKey = 1
            ) {
                songPager
            }.flow.cachedIn(viewModelScope).collect {
                _songs.value = it
            }
        }
    }

    fun onEvent(event: ArtistAllUiEvent) {
        when (event) {
            is ArtistAllUiEvent.EmitToast -> {

            }

            ArtistAllUiEvent.SomethingWentWrong -> {

            }

            is ArtistAllUiEvent.ItemClick -> {
                when (event) {
                    is ArtistAllUiEvent.ItemClick.AlbumClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    route = Screens.SongView.route,
                                    type = ItemsType.ALBUM,
                                    id = event.id,
                                    isApiCall = true
                                )
                            )
                        }
                    }

                    is ArtistAllUiEvent.ItemClick.SongClick -> {
                        // todo player screen
                    }
                }
            }
        }
    }
}