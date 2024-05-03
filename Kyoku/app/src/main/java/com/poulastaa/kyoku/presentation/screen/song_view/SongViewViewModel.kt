package com.poulastaa.kyoku.presentation.screen.song_view

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponseStatus
import com.poulastaa.kyoku.data.model.screens.common.ItemsType
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiEvent
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiModel
import com.poulastaa.kyoku.data.model.screens.song_view.SongViewUiState
import com.poulastaa.kyoku.data.model.screens.song_view.UiArtist
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.BitmapConverter
import com.poulastaa.kyoku.utils.toUiPlaylistSong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
            val authType = when (ds.readAuthType().first()) {
                AuthType.SESSION_AUTH.name -> true
                AuthType.JWT_AUTH.name -> false
                else -> return@launch
            }

            state = state.copy(
                isCooke = authType
            )
        }
    }

    fun savePlayingSongId(id: Long) {
        when (state.type) {
            ItemsType.PLAYLIST -> {
                if (state.data.playlist.listOfSong.isNotEmpty()) {
                    state = state.copy(
                        data = state.data.copy(
                            playlist = state.data.playlist.copy(
                                listOfSong = state.data.playlist.listOfSong.map {
                                    if (it.songId == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
                }
            }

            ItemsType.ALBUM -> {
                if (state.data.album.listOfSong.isNotEmpty()) {
                    state = state.copy(
                        data = state.data.copy(
                            album = state.data.album.copy(
                                listOfSong = state.data.album.listOfSong.map {
                                    if (it.songId == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
                }
            }

            ItemsType.ALBUM_PREV -> {
                if (state.data.album.listOfSong.isNotEmpty()) {
                    state = state.copy(
                        data = state.data.copy(
                            album = state.data.album.copy(
                                listOfSong = state.data.album.listOfSong.map {
                                    if (it.songId == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
                }
            }


            ItemsType.ARTIST_MIX -> {
                if (state.data.dailyMixOrArtistMix.listOfSong.isNotEmpty()) {
                    state = state.copy(
                        data = state.data.copy(
                            dailyMixOrArtistMix = state.data.dailyMixOrArtistMix.copy(
                                listOfSong = state.data.dailyMixOrArtistMix.listOfSong.map {
                                    if (it.songId == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
                }
            }

            ItemsType.DAILY_MIX -> {
                if (state.data.dailyMixOrArtistMix.listOfSong.isNotEmpty()) {
                    state = state.copy(
                        data = state.data.copy(
                            dailyMixOrArtistMix = state.data.dailyMixOrArtistMix.copy(
                                listOfSong = state.data.dailyMixOrArtistMix.listOfSong.map {
                                    if (it.songId == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
                }
            }

            ItemsType.FAVOURITE -> {
                if (state.data.favourites.listOfSong.isNotEmpty()) {
                    state = state.copy(
                        data = state.data.copy(
                            favourites = state.data.favourites.copy(
                                listOfSong = state.data.favourites.listOfSong.map {
                                    if (it.songId == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
                }
            }


            ItemsType.ARTIST -> {
                if (state.data.artist.listOfSong.isNotEmpty())
                    state = state.copy(
                        data = state.data.copy(
                            artist = state.data.artist.copy(
                                listOfSong = state.data.artist.listOfSong.map {
                                    if (it.id.toLong() == id) it.copy(
                                        isPlaying = true
                                    ) else it.copy(
                                        isPlaying = false
                                    )
                                }
                            )
                        )
                    )
            }

            ItemsType.ARTIST_MORE -> {

            }

            ItemsType.SONG -> {

            }

            ItemsType.HISTORY -> {

            }

            ItemsType.ERR -> return
        }
    }

    fun loadData(
        typeString: String,
        id: Long,
        name: String,
        isApiCall: Boolean,
        context: Context
    ) {
        if (state.isLoading) {
            when (getItemType(typeString)) {
                ItemsType.PLAYLIST -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        db.getPlaylist(id).collect {
                            state = state.copy(
                                type = if (it.isEmpty()) ItemsType.ERR else ItemsType.PLAYLIST,
                                data = state.data.copy(
                                    playlist = SongViewUiModel(
                                        name = db.getPlaylistName(id),
                                        totalTime = async {
                                            it.map { single ->
                                                (single.totalTime.toFloatOrNull() ?: 0F) / 1000 / 60
                                            }.sum().toInt().toString()
                                        }.await(),
                                        listOfSong = it
                                    )
                                )
                            )
                        }
                    }
                }

                ItemsType.ALBUM -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        db.getAlbum(id).let {
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
                        val album = db.getAlbum(id)

                        if (album.listOfSong.isNotEmpty()) {
                            state = state.copy(
                                type = ItemsType.ALBUM,
                                data = state.data.copy(
                                    album = album
                                )
                            )
                        } else {
                            db.setValues(
                                context = context,
                                header = state.headerValue
                            )

                            val resultDef = async { api.getAlbum(id) }
                            db.removeAllFromReqAlbum()

                            val result = resultDef.await()

                            if (result.listOfSongs.isEmpty()) {
                                onEvent(SongViewUiEvent.SomethingWentWrong)

                                return@launch
                            }

                            val songs = result.listOfSongs.map {
                                async {
                                    it to (ImageLoader(context).execute(
                                        ImageRequest.Builder(context)
                                            .addHeader(
                                                if (!state.headerValue.startsWith("B")) "Cookie" else "Authorization",
                                                state.headerValue
                                            )
                                            .data(it.coverImage)
                                            .build()
                                    )).drawable
                                }
                            }.awaitAll().map {
                                try {
                                    it.first to BitmapConverter.encodeToSting((it.second as BitmapDrawable).bitmap)
                                } catch (e: Exception) {
                                    it.first to it.first.coverImage
                                }
                            }.map {
                                it.first.copy(
                                    coverImage = it.second
                                )
                            }

                            async {
                                db.insertIntoReqAlbum(
                                    entry = result.copy(
                                        listOfSongs = songs
                                    )
                                )
                            }.await()

                            db.readFromReqAlbum().collect {
                                state = state.copy(
                                    data = state.data.copy(
                                        album = it.groupBy { entry ->
                                            entry.albumId
                                        }.map { entry ->
                                            SongViewUiModel(
                                                id = entry.key,
                                                name = entry.value[0].albumName,
                                                totalTime = async {
                                                    entry.value.map { single ->
                                                        (single.totalTime.toFloatOrNull()
                                                            ?: 0F) / 1000 / 60
                                                    }.sum().toInt().toString()
                                                }.await(),
                                                listOfSong = entry.value.toUiPlaylistSong()
                                            )
                                        }.firstOrNull() ?: SongViewUiModel()
                                    )
                                )
                            }
                        }
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
                        if (db.checkIfArtistMixIsEmpty()) {
                            val response = api.getArtistMix()

                            if (response.isEmpty()) {
                                state = state.copy(
                                    type = ItemsType.ERR
                                )

                                return@launch
                            }

                            db.setValues(
                                context = context,
                                header = state.headerValue
                            )

                            async { db.insertIntoArtistMix(response) }.await()
                        }

                        db.readAllArtistMix().collect {
                            state = state.copy(
                                type = ItemsType.ARTIST_MIX,
                                data = state.data.copy(
                                    dailyMixOrArtistMix = SongViewUiModel(
                                        name = "Artist Mix",
                                        totalTime = async {
                                            it.map { single ->
                                                (single.totalTime.toFloatOrNull() ?: 0F) / 1000 / 60
                                            }.sum().toInt().toString()
                                        }.await(),
                                        listOfSong = it
                                    )
                                )
                            )
                        }
                    }
                }

                ItemsType.DAILY_MIX -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (db.checkIfDailyMixTableEmpty()) {
                            val response = api.getDailyMix()

                            if (response.isEmpty()) {
                                state = state.copy(
                                    type = ItemsType.ERR
                                )

                                return@launch
                            }

                            db.setValues(
                                context = context,
                                header = state.headerValue
                            )

                            async {
                                db.insertIntoDailyMix(entrys = response)
                            }.await()
                        }

                        db.readAllDailyMix().collect {
                            state = state.copy(
                                type = ItemsType.DAILY_MIX,
                                data = state.data.copy(
                                    dailyMixOrArtistMix = SongViewUiModel(
                                        name = "Daily Mix",
                                        totalTime = async {
                                            it.map { single ->
                                                (single.totalTime.toFloatOrNull() ?: 0F) / 1000 / 60
                                            }.sum().toInt().toString()
                                        }.await(),
                                        listOfSong = it
                                    )
                                )
                            )
                        }
                    }
                }

                ItemsType.ARTIST_MORE -> {
                    viewModelScope.launch(Dispatchers.IO) {

                    }
                }

                ItemsType.FAVOURITE -> {
                    viewModelScope.launch(Dispatchers.IO) {
                        db.getAllFavouriteSongs().collect {
                            state = state.copy(
                                type = if (it.isEmpty()) ItemsType.ERR else ItemsType.FAVOURITE,
                                data = state.data.copy(
                                    favourites = SongViewUiModel(
                                        name = "Favourites",
                                        totalTime = async {
                                            it.map { single ->
                                                (single.totalTime.toFloatOrNull() ?: 0F) / 1000 / 60
                                            }.sum().toInt().toString()
                                        }.await(),
                                        listOfSong = it
                                    )
                                )
                            )
                        }
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
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            SongViewUiEvent.SomethingWentWrong -> {
                onEvent(SongViewUiEvent.EmitToast("Opp's something went wrong"))
            }

            is SongViewUiEvent.PlayControlClick -> {
                when (event) {
                    is SongViewUiEvent.PlayControlClick.DownloadClick -> {
                        // todo add logic to download
                        Log.d("download clicked", event.toString())
                    }

                    is SongViewUiEvent.PlayControlClick.PlayClick -> {
                        when (event.type) {
                            UiEvent.PlayType.PLAYLIST -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val playlistId = db.getPlaylistId(event.name)

                                    if (playlistId == null) {
                                        onEvent(SongViewUiEvent.SomethingWentWrong)

                                        return@launch
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = playlistId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ALBUM -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val albumId = db.getAlbumId(event.name)

                                    if (albumId == null) {
                                        onEvent(SongViewUiEvent.SomethingWentWrong)

                                        return@launch
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = albumId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ALBUM_PREV -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val albumId = db.getPrevAlbumId(event.name)

                                    if (albumId == null && state.data.album.id == -1L) {
                                        onEvent(SongViewUiEvent.SomethingWentWrong)

                                        return@launch
                                    }



                                    if (albumId != null) {
                                        _uiEvent.send(
                                            UiEvent.Play(
                                                otherId = albumId,
                                                playType = event.type
                                            )
                                        )
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = state.data.album.id,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ARTIST_MIX -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.Play(
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.DAILY_MIX -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.Play(
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.FAVOURITE -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.Play(
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ARTIST_MORE_ALL_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val artistId = db.getArtistId(event.name)

                                    val songIdList = state.data.artist.listOfSong.map {
                                        it.id.toLong()
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = artistId,
                                            songIdList = songIdList,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            else -> Unit
                        }
                    }

                    is SongViewUiEvent.PlayControlClick.ShuffleClick -> {
                        // todo add logic to shuffle
                        Log.d("ShuffleClick clicked", event.toString())
                    }

                    is SongViewUiEvent.PlayControlClick.SongPlayClick -> {
                        when (event.type) {
                            UiEvent.PlayType.PLAYLIST_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val playlistId = db.getPlaylistId(event.name)

                                    if (playlistId == null) {
                                        onEvent(SongViewUiEvent.SomethingWentWrong)

                                        return@launch
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = playlistId,
                                            songId = event.songId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ALBUM_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val albumId = db.getAlbumId(event.name)

                                    if (albumId == null) {
                                        onEvent(SongViewUiEvent.SomethingWentWrong)

                                        return@launch
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = albumId,
                                            songId = event.songId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ALBUM_PREV_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val albumId = db.getPrevAlbumId(event.name)

                                    if (albumId == null && state.data.album.id == -1L) {
                                        onEvent(SongViewUiEvent.SomethingWentWrong)

                                        return@launch
                                    }



                                    if (albumId != null) {
                                        _uiEvent.send(
                                            UiEvent.Play(
                                                otherId = albumId,
                                                songId = event.songId,
                                                playType = event.type
                                            )
                                        )
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = state.data.album.id,
                                            songId = event.songId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ARTIST_MIX_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.Play(
                                            songId = event.songId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.DAILY_MIX_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.Play(
                                            songId = event.songId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }


                            UiEvent.PlayType.FAVOURITE_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    _uiEvent.send(
                                        UiEvent.Play(
                                            songId = event.songId,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            UiEvent.PlayType.ARTIST_MORE_ONE_SONG -> {
                                viewModelScope.launch(Dispatchers.IO) {
                                    val artistId = db.getArtistId(event.name)

                                    val songIdList = state.data.artist.listOfSong.map {
                                        it.id.toLong()
                                    }

                                    _uiEvent.send(
                                        UiEvent.Play(
                                            otherId = artistId,
                                            songId = event.songId,
                                            songIdList = songIdList,
                                            playType = event.type
                                        )
                                    )
                                }
                            }

                            else -> Unit
                        }
                    }
                }
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
        ItemsType.DAILY_MIX.title -> ItemsType.DAILY_MIX
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


    fun removeDbEntrys() {
        when (state.type) {
            ItemsType.ALBUM_PREV -> {
                db.removeAllFromReqAlbum()
            }

            else -> Unit
        }
    }
}