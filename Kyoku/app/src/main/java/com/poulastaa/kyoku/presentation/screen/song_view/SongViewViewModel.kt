package com.poulastaa.kyoku.presentation.screen.song_view

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponseStatus
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiState
import com.poulastaa.kyoku.data.model.screens.song_view.UiAlbum
import com.poulastaa.kyoku.data.model.screens.song_view.UiArtist
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylist
import com.poulastaa.kyoku.data.model.screens.song_view.UiSong
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
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
                if (it != NetworkObserver.STATUS.AVAILABLE)
                    state = state.copy(
                        isInternetAvailable = false,
                        isInternetError = true
                    )
            }
        }
    }

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(SongViewUiState())
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
                        state = state.copy(
                            type = ItemsType.ERR
                        )
                        false
                    }
                }
            )
        }
    }

    fun loadData(
        typeString: String,
        id: Long,
        name: String,
        isApiCall: Boolean
    ) {
        if (state.isLoading) {
            when (getItemType(typeString)) {
                ItemsType.PLAYLIST -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        db.getPlaylist(id).collect {
                            state = state.copy(
                                type = if (it.isEmpty()) ItemsType.ERR else ItemsType.PLAYLIST,
                                data = state.data.copy(
                                    playlist = it.groupBy { song -> song.name }.map { entry ->
                                        UiPlaylist(
                                            name = entry.key,
                                            listOfSong = entry.value
                                        )
                                    }.firstOrNull() ?: UiPlaylist()
                                )
                            )
                        }
                    }
                }

                ItemsType.ALBUM -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        when (isApiCall) {
                            true -> getAlbumFromApi(id)
                            false -> db.getAlbum(name)
                        }.let {
                            state = state.copy(
                                type = if (it.listOfSong.isEmpty()) ItemsType.ERR else ItemsType.ALBUM,
                                data = state.data.copy(
                                    album = it
                                )
                            )
                        }
                    }
                }

                ItemsType.ALBUM_PREV -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }

                ItemsType.ARTIST -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        val responseDiffered = async {
                            api.artistMostPopularReq(
                                req = ArtistMostPopularSongReq(
                                    id = id,
                                    name = name
                                )
                            )
                        }

                        val coverImageDiffered = async {
                            db.getArtistCoverImage(id)
                        }

                        val response = responseDiffered.await()
                        val cover = coverImageDiffered.await()


                        when (response.status) {
                            HomeResponseStatus.SUCCESS -> {
                                state = if (response.listOfSong.isNotEmpty())
                                    state.copy(
                                        type = ItemsType.ARTIST,
                                        data = state.data.copy(
                                            artist = UiArtist(
                                                name = name,
                                                coverImage = cover,
                                                points = response.points,
                                                listOfSong = response.listOfSong
                                            )
                                        )
                                    )
                                else state.copy(
                                    type = ItemsType.ERR
                                )
                            }

                            HomeResponseStatus.FAILURE -> {
                                state = state.copy(
                                    type = ItemsType.ERR
                                )
                            }
                        }
                    }
                }

                ItemsType.ARTIST_MIX -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }

                ItemsType.ARTIST_MORE -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }

                ItemsType.FAVOURITE -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        val favourites = db.getAllFavouriteSongs()

                        state = state.copy(
                            type = if (favourites.isEmpty()) ItemsType.ERR else ItemsType.FAVOURITE,
                            data = state.data.copy(
                                favourites = favourites
                            )
                        )
                    }
                }

                ItemsType.SONG -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }

                ItemsType.HISTORY -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }

                ItemsType.ERR -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }
            }.let {
                state = state.copy(
                    isLoading = false
                )
            }
        }
    }

    fun onEvent(event: SongViewUiEvent) {
        when (event) {
            is SongViewUiEvent.EmitToast -> {

            }


            SongViewUiEvent.SomethingWentWrong -> {

            }

            is SongViewUiEvent.ItemClick -> {
                when (event) {
                    is SongViewUiEvent.ItemClick.ViewAllFromArtist -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            _uiEvent.send(
                                UiEvent.NavigateWithData(
                                    route = Screens.AllFromArtist.route,
                                    name = event.name
                                )
                            )
                        }
                    }
                }
            }
        }
    }


    private fun getItemType(type: String): ItemsType = when (type) {
        ItemsType.PLAYLIST.title -> ItemsType.PLAYLIST
        ItemsType.ALBUM.title -> ItemsType.ALBUM
        ItemsType.ALBUM_PREV.title -> ItemsType.ALBUM_PREV
        ItemsType.ARTIST.title -> ItemsType.ARTIST
        ItemsType.ARTIST_MIX.title -> ItemsType.ARTIST_MIX
        ItemsType.ARTIST_MORE.title -> ItemsType.ARTIST_MORE
        ItemsType.FAVOURITE.title -> ItemsType.FAVOURITE
        ItemsType.SONG.title -> ItemsType.SONG
        ItemsType.HISTORY.title -> ItemsType.HISTORY
        else -> ItemsType.ERR
    }.let {
        state = state.copy(
            type = it
        )

        it
    }

    private suspend fun getAlbumFromApi(id: Long) = api.getAlbum(id).let { album ->
        UiAlbum(
            name = album.name,
            listOfSong = album.listOfSongs.map { song ->
                UiSong(
                    id = song.id.toLong(),
                    title = song.title,
                    artist = song.artist,
                    album = album.name,
                    coverImage = song.coverImage
                )
            }
        )
    }

}