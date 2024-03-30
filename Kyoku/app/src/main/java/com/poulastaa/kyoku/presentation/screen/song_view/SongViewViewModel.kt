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
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiState
import com.poulastaa.kyoku.data.model.screens.song_view.UiArtist
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylist
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
        name: String
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
                                    }.first()
                                )
                            )
                        }
                    }
                }

                ItemsType.ALBUM -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        val album = db.getAlbum(name)

                        state = state.copy(
                            type = if (album.listOfSong.isEmpty()) ItemsType.ERR else ItemsType.ALBUM,
                            data = state.data.copy(
                                album = album
                            )
                        )
                    }
                }

                ItemsType.ALBUM_PREV -> {

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
                                state = state.copy(
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

                }

                ItemsType.ARTIST_MORE -> {

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

                }

                ItemsType.HISTORY -> {

                }

                ItemsType.ERR -> {

                }
            }

            viewModelScope.launch(Dispatchers.IO) {
                delay(600)
                state = state.copy(
                    isLoading = false
                )
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
}