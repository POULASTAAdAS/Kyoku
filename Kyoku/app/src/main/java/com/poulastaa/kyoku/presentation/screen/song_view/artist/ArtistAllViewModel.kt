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
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.home.HomeLongClickType
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.data.model.screens.song_view.AlbumType
import com.poulastaa.kyoku.data.model.screens.song_view.AllArtistSongType
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.ArtistAllUiState
import com.poulastaa.kyoku.data.model.screens.song_view.BottomSheetData
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.domain.usecase.ArtistAlbumPagingSource
import com.poulastaa.kyoku.domain.usecase.ArtistSongPagingSource
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl,
    private val api: ServiceRepository
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
        getUrl(name)

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
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            ArtistAllUiEvent.SomethingWentWrong -> {
                onEvent(ArtistAllUiEvent.EmitToast("Opp's something went wrong"))
            }

            is ArtistAllUiEvent.ItemClick -> {
                when (event) {
                    is ArtistAllUiEvent.ItemClick.AlbumClick -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    route = Screens.SongView.route,
                                    itemsType = ItemsType.ALBUM_PREV,
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

            is ArtistAllUiEvent.ItemLongClick -> {
                if (event.id == -1L) {
                    onEvent(ArtistAllUiEvent.SomethingWentWrong)

                    return
                }



                viewModelScope.launch(Dispatchers.IO) {
                    val status = when (event.type) {
                        BottomSheetData.BottomSheetDataType.ALBUM -> {
                            db.checkIfAlbumAlreadyInLibrary(event.id)
                        }

                        BottomSheetData.BottomSheetDataType.SONG -> {
                            db.checkIfSongAlreadyInFavourite(event.id)
                        }
                    }

                    state = state.copy(
                        bottomSheetData = state.bottomSheetData.copy(
                            url = event.url,
                            name = event.name,
                            id = event.id,
                            type = event.type,
                            operation = status
                        )
                    )
                }.let {
                    state = state.copy(
                        isBottomSheetOpen = true
                    )
                }
            }

            is ArtistAllUiEvent.BottomSheetItemClick -> {
                state = state.copy(
                    isBottomSheetOpen = false
                )

                when (event) {
                    is ArtistAllUiEvent.BottomSheetItemClick.AlbumClick -> {
                        when (event.type) {
                            AlbumType.PLAY_ALBUM -> {

                            }

                            AlbumType.ADD_TO_LIBRARY_ALBUM -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val response = api.getAlbum(event.id)

                                    if (response.listOfSongs.isEmpty()) {
                                        onEvent(ArtistAllUiEvent.EmitToast("Opp's something went wrong"))

                                        return@launch
                                    }
                                    onEvent(ArtistAllUiEvent.EmitToast("${response.name} added to library"))

                                    async { db.insertIntoAlbum(listOf(response)) }.await()

                                    api.editAlbum(event.id, true)
                                }
                            }

                            AlbumType.REMOVE_FROM_LIBRARY_ALBUM -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val apiCallDef = async { api.editAlbum(event.id, true) }
                                    val albumDef = async { db.getAlbumOnAlbumId(event.id) }

                                    apiCallDef.await()
                                    val album = albumDef.await()

                                    db.removeAlbum(album.id)

                                    onEvent(ArtistAllUiEvent.EmitToast("${album.name} removed from library"))
                                }
                            }

                            AlbumType.ADD_AS_PLAYLIST -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.NavigateWithData(
                                            route = Screens.CreatePlaylist.route,
                                            id = event.id,
                                            name = event.name,
                                            longClickType = HomeLongClickType.ALBUM_PREV.name
                                        )
                                    )
                                }
                            }

                            AlbumType.DOWNLOAD_ALBUM -> {

                            }
                        }
                    }

                    is ArtistAllUiEvent.BottomSheetItemClick.SongClick -> {
                        when (event.type) {
                            AllArtistSongType.PLAY_SONG -> {

                            }

                            AllArtistSongType.ADD_TO_FAVOURITE -> {
                                if (!state.isInternetAvailable) {
                                    onEvent(ArtistAllUiEvent.EmitToast("Please check your Internet connection"))

                                    return
                                }

                                viewModelScope.launch(Dispatchers.IO) {
                                    val responseSong = api.addSongToFavourite(event.id)

                                    if (responseSong.id == -1L) {
                                        onEvent(ArtistAllUiEvent.SomethingWentWrong)

                                        return@launch
                                    }
                                    onEvent(ArtistAllUiEvent.EmitToast("${responseSong.title} added to favourite"))

                                    async { db.insertIntoFavourite(list = listOf(responseSong)) }.await()
                                }
                            }

                            AllArtistSongType.REMOVE_FROM_FAVOURITE -> {
                                if (!state.isInternetAvailable) {
                                    onEvent(ArtistAllUiEvent.EmitToast("Please check your Internet connection"))

                                    return
                                }

                                viewModelScope.launch(Dispatchers.IO) {
                                    val responseSong = api.removeFromFavourite(event.id)

                                    if (!responseSong) {
                                        onEvent(ArtistAllUiEvent.SomethingWentWrong)

                                        return@launch
                                    }

                                    db.removeFromFavourite(event.id)

                                    onEvent(ArtistAllUiEvent.EmitToast("${event.name} removed from favourite"))
                                }
                            }

                            AllArtistSongType.ADD_TO_PLAYLIST -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.NavigateWithData(
                                            route = Screens.EditPlaylist.route,
                                            songType = SongType.ARTIST_SONG,
                                            id = event.id
                                        )
                                    )
                                }
                            }
                        }
                    }

                    ArtistAllUiEvent.BottomSheetItemClick.CancelClick -> Unit
                }
            }
        }
    }

    private fun getUrl(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            state = state.copy(
                artistUrl = db.getArtistCoverImage(name) ?: ""
            )
        }
    }
}