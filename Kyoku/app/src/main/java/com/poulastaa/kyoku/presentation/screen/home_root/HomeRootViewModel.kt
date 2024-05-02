package com.poulastaa.kyoku.presentation.screen.home_root

import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.UiEvent
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeScreenBottomNavigation
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.data.model.home_nav_drawer.Player
import com.poulastaa.kyoku.data.model.home_nav_drawer.PlayingSongInfo
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.data.model.screens.player.DragAnchors
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.data.model.screens.player.PlayerUiEvent
import com.poulastaa.kyoku.data.model.screens.player.PlayerUiState
import com.poulastaa.kyoku.data.model.screens.song_view.UiPlaylistSong
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.player.service.AudioServiceHandler
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toPlayerData
import com.poulastaa.kyoku.utils.toPlayerDataIfAny
import com.poulastaa.kyoku.utils.toPlayingQueueTable
import com.poulastaa.kyoku.utils.toViewArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

private const val DAILY_MIX = "Daily Mix"
private const val ARTIST_MIX = "Artist Mix"
private const val FAVOURITE = "Favourite"

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class HomeRootViewModel @Inject constructor(
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl,
    private val api: ServiceRepository,
    private val player: AudioServiceHandler
) : ViewModel() {
    private fun readAccessToken() {
        viewModelScope.launch {
            ds.readTokenOrCookie().collect {
                Log.d("cookie", it)

                state = state.copy(
                    headerValue = it
                )
            }
        }
    }

    private fun readAuthType() {
        viewModelScope.launch {
            val authType = ds.readAuthType().first()

            state = state.copy(
                isCookie = authType == AuthType.SESSION_AUTH.name
            )
        }
    }

    private fun readUserName() {
        viewModelScope.launch {
            ds.readUsername().collect {
                state = state.copy(
                    userName = it
                )
            }
        }
    }

    private fun readProfilePicUrl() {
        viewModelScope.launch {
            ds.readProfilePic().collect {
                state = state.copy(
                    profilePicUrl = it
                )
            }
        }
    }

    private fun setHomeTopBarTitle() {
        viewModelScope.launch {
            val localTime = LocalDateTime.now().toLocalTime()

            val currentTime = localTime.format(DateTimeFormatter.ofPattern("hh")).toInt()
            val status = localTime.format(DateTimeFormatter.ofPattern("a"))

            if (status.uppercase() == "AM") {
                state = if (currentTime == 12) {
                    state.copy(
                        homeTopBarTitle = "Mid Night"
                    )
                } else if (currentTime >= 4) {
                    state.copy(
                        homeTopBarTitle = "Good Morning"
                    )
                } else {
                    state.copy(
                        homeTopBarTitle = "Night Owl"
                    )
                }
            } else {
                state = if (currentTime <= 5 || currentTime == 12) {
                    state.copy(
                        homeTopBarTitle = "Good Afternoon"
                    )
                } else if (currentTime in 6..10) {
                    state.copy(
                        homeTopBarTitle = "Good Evening"
                    )
                } else if (currentTime in 10..11) {
                    state.copy(
                        homeTopBarTitle = "Good Night"
                    )
                } else {
                    state.copy(
                        homeTopBarTitle = "Night Owl"
                    )
                }
            }
        }
    }


    private var getArtistJob: Job? = null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var state by mutableStateOf(HomeRootUiState())
        private set

    init {
        readAccessToken()
        readAuthType()
        readUserName()
        readProfilePicUrl()
        setHomeTopBarTitle()
    }

    init {
        viewModelScope.launch {
            player.playerUiState.collect { event ->
                when (event) {
                    is PlayerUiState.Buffering -> {
                        Log.d("update buffer", event.value.toString())
                    }

                    is PlayerUiState.CurrentPlayingSongId -> {
                        val song = state.player.allSong.firstNotNullOfOrNull {
                            if (it.playerSong.id == event.id) it.playerSong else null
                        }

                        if (song != null) {
                            state = state.copy(
                                player = state.player.copy(
                                    colors = listOf(
                                        song.colorOne,
                                        song.colorTwo,
                                        song.colorThree
                                    ),
                                    playingSong = song
                                )
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = state.player.allSong.map {
                                        if (it.playerSong.id == song.id) it.copy(
                                            isPlaying = true
                                        )
                                        else it.copy(
                                            isPlaying = false
                                        )
                                    }
                                )
                            )

                            getArtistJob?.cancel()
                            getArtistJob = getArtistJob(song.id)
                        }
                    }

                    PlayerUiState.Initial -> {
                        Log.d("update Initial", event.toString())
                    }

                    is PlayerUiState.Playing -> {
                        Log.d("update Playing", event.isPlaying.toString())

                        state = state.copy(
                            player = state.player.copy(
                                isPlaying = event.isPlaying
                            )
                        )
                    }

                    is PlayerUiState.Progress -> {
                        state = state.copy(
                            player = state.player.copy(
                                progress = try {
                                    ((event.value.toFloat() / state.player.playingSong.totalInMili) * 100f)
                                } catch (_: Exception) {
                                    0f
                                },
                                playingSong = state.player.playingSong.copy(
                                    currentInMin = try {
                                        millisecondsToMinutesAndSeconds(event.value)
                                    } catch (_: Exception) {
                                        "-.-"
                                    }
                                )
                            )
                        )
                    }

                    is PlayerUiState.Ready -> {
                        Log.d("update Ready", event.duration.toString())
                    }
                }
            }
        }
    }

    fun loadSongIfAny() {
        if (state.isFirst)
            viewModelScope.launch(Dispatchers.IO) {
                db.readAllFromPlayingQueue().first().let {
                    if (it.isEmpty()) return@launch

                    val result = it.toPlayerDataIfAny()

                    state = state.copy(
                        isFirst = false,
                        player = state.player.copy(
                            isLoading = false,
                            isSmallPlayer = true,
                            allSong = result
                        )
                    )

                    withContext(Dispatchers.Main) {
                        result.map { song ->
                            song.playerSong
                        }.setMediaItems()
                    }
                }
            }
    }

    fun onEvent(event: HomeRootUiEvent) {
        when (event) {
            is HomeRootUiEvent.EmitToast -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(UiEvent.ShowToast(event.message))
                }
            }

            HomeRootUiEvent.SomethingWentWrong -> {
                viewModelScope.launch(Dispatchers.IO) {
                    onEvent(HomeRootUiEvent.EmitToast("Opp's something went wrong"))
                }
            }

            is HomeRootUiEvent.Navigate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(
                        element = UiEvent.Navigate(
                            route = event.route
                        )
                    )
                }
            }

            is HomeRootUiEvent.NavigateWithData -> {
                if (event.route == Screens.Player.route) return
                else viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(
                        UiEvent.NavigateWithData(
                            route = event.route,
                            itemsType = event.type,
                            id = event.id,
                            name = event.name,
                            longClickType = event.longClickType,
                            isApiCall = event.isApiCall
                        )
                    )
                }
            }

            is HomeRootUiEvent.Play -> {
                db.setValues(
                    context = event.context,
                    header = state.headerValue
                )

                state = state.copy(
                    player = state.player.copy(
                        isSmallPlayer = true,
                        isLoading = true
                    )
                )

                when (event.playType) {
                    UiEvent.PlayType.HISTORY_SONG -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            if (db.checkIfAlreadyInPlayingQueue(event.songId) == null) {
                                val song = api.getSongOnId(event.songId)

                                if (song.id == -1L) {
                                    onEvent(HomeRootUiEvent.SomethingWentWrong)

                                    state = state.copy(
                                        player = state.player.copy(
                                            isSmallPlayer = false,
                                            isLoading = false
                                        )
                                    )

                                    return@launch
                                }
                                async {
                                    db.insertIntoPlayingQueueTable(
                                        song,
                                        songType = SongType.ARTIST_SONG
                                    )
                                }.await()

                                val list = db.readAllFromPlayingQueue().first().toPlayerData()

                                CoroutineScope(Dispatchers.Main).launch {
                                    player.onEvent(PlayerUiEvent.Stop)
                                    list[0].playerSong.setMediaItem(song.coverImage)
                                    player.onEvent(PlayerUiEvent.PlayPause)
                                }

                                state = state.copy(
                                    player = state.player.copy(
                                        allSong = list,
                                        info = PlayingSongInfo(
                                            typeName = "Recently Played"
                                        )
                                    )
                                )
                            } else {
                                viewModelScope.launch(Dispatchers.Main) {
                                    player.onEvent(PlayerUiEvent.SeekTo(0))
                                }
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ARTIST_SONG -> {
                        viewModelScope.launch(Dispatchers.IO) {
                            if (db.checkIfAlreadyInPlayingQueue(event.songId) == null) {
                                val songDef = async { api.getSongOnId(event.songId) }

                                val artistDef = async { db.getArtistOnId(event.otherId) }

                                val song = songDef.await()
                                val artist = artistDef.await()

                                if (song.id == -1L) {
                                    onEvent(HomeRootUiEvent.SomethingWentWrong)

                                    state = state.copy(
                                        player = state.player.copy(
                                            isSmallPlayer = false,
                                            isLoading = false
                                        )
                                    )

                                    return@launch
                                }

                                async {
                                    db.insertIntoPlayingQueueTable(
                                        song,
                                        songType = SongType.ARTIST_SONG
                                    )
                                }.await()

                                val list = db.readAllFromPlayingQueue().first().toPlayerData()

                                CoroutineScope(Dispatchers.Main).launch {
                                    player.onEvent(PlayerUiEvent.Stop)
                                    list[0].playerSong.setMediaItem(song.coverImage)
                                    player.onEvent(PlayerUiEvent.PlayPause)
                                }

                                state = state.copy(
                                    player = state.player.copy(
                                        allSong = list,
                                        info = PlayingSongInfo(
                                            id = artist?.artistId ?: -1,
                                            typeName = artist?.name ?: "Kyoku"
                                        )
                                    )
                                )
                            } else {
                                viewModelScope.launch(Dispatchers.Main) {
                                    player.onEvent(PlayerUiEvent.SeekTo(0))
                                }
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.PLAYLIST -> {
                        if (state.player.info.id == event.otherId) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(0, 0))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val playlistDef =
                                async { db.getPlaylist(id = event.otherId).first() }
                            val playlistNameDef = async { db.getPlaylistName(event.otherId) }

                            clear.await()
                            val playlistSongs = playlistDef.await().toPlayerData()
                            val playlist = event.otherId to playlistNameDef.await()

                            if (playlistSongs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                playlistSongs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                UiEvent.PlayType.PLAYLIST
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = playlistSongs,
                                    info = PlayingSongInfo(
                                        id = playlist.first,
                                        typeName = playlist.second
                                    )
                                )
                            )

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                playlistSongs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.PlayPause)
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.PLAYLIST_SONG -> {
                        val index = state.player.allSong.map { it.playerSong.id }
                            .indexOf(event.songId)

                        val oldSong = try { // same playlist song
                            state.player.allSong[index]
                        } catch (_: Exception) {
                            null
                        }

                        if (oldSong != null) viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.SeekToSong(index))

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                        else viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val playlistDef =
                                async { db.getPlaylist(id = event.otherId).first() }
                            val playlistNameDef = async { db.getPlaylistName(event.otherId) }

                            clear.await()
                            val playlistSongs = playlistDef.await().toPlayerData()
                            val playlist = event.otherId to playlistNameDef.await()


                            val playingSong = playlistSongs.firstNotNullOfOrNull {
                                if (it.playerSong.id == event.songId) it else null
                            }

                            if (playingSong == null) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                playlistSongs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                UiEvent.PlayType.PLAYLIST
                            )


                            val i = playlistSongs.map {
                                it.playerSong.id
                            }.indexOf(event.songId)

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                playlistSongs.map { it.playerSong }.setMediaItems()
                                player.onEvent(PlayerUiEvent.SeekToSong(i))
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false,
                                    allSong = playlistSongs,
                                    info = PlayingSongInfo(
                                        id = playlist.first,
                                        typeName = playlist.second
                                    )
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ALBUM -> {
                        if (state.player.info.id == event.otherId) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(0, 0))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val albumSongsDef = async { db.getAlbum(event.otherId) }
                            val albumDef = async { db.getAlbumOnAlbumId(event.otherId) }

                            clear.await()
                            val album = albumDef.await()
                            val albumSongs = albumSongsDef.await().listOfSong.toPlayerData()

                            if (albumSongs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable( // todo
                                entrys = albumSongs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.ALBUM
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = albumSongs,
                                    info = PlayingSongInfo(
                                        id = album.albumId,
                                        typeName = album.name
                                    )
                                )
                            )

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                albumSongs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.PlayPause)
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ALBUM_SONG -> {
                        val index = state.player.allSong.map { it.playerSong.id }
                            .indexOf(event.songId)

                        val oldSong = try { // same album song
                            state.player.allSong[index]
                        } catch (_: Exception) {
                            null
                        }

                        if (oldSong != null) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(index))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val albumSongsDef = async { db.getAlbum(event.otherId) }
                            val albumDef = async { db.getAlbumOnAlbumId(event.otherId) }

                            clear.await()
                            val album = albumDef.await()
                            val albumSongs = albumSongsDef.await().listOfSong.toPlayerData()

                            if (albumSongs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            val i = albumSongs.map {
                                it.playerSong.id
                            }.indexOf(event.songId)

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                albumSongs.map { it.playerSong }.setMediaItems()
                                player.onEvent(PlayerUiEvent.SeekToSong(i))
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false,
                                    allSong = albumSongs,
                                    info = PlayingSongInfo(
                                        id = album.albumId,
                                        typeName = album.name
                                    )
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ALBUM_PREV -> {
                        if (event.otherId == state.player.info.id) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(0, 0))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val prevAlbumDef = async { db.getPrevAlbum(event.otherId) }

                            clear.await()
                            val pair = prevAlbumDef.await().groupBy { it.albumId }.map {
                                PlayingSongInfo(
                                    id = it.key,
                                    typeName = it.value[0].albumName
                                ) to it.value.map { song ->
                                    UiPlaylistSong(
                                        isPlaying = false,
                                        songId = song.songId,
                                        title = song.title,
                                        artist = song.artist,
                                        coverImage = song.coverImage,
                                        masterPlaylistUrl = song.masterPlaylistUrl,
                                        totalTime = song.totalTime
                                    )
                                }
                            }.first()


                            if (pair.second.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            val songs = pair.second.toPlayerData(
                                isDarkThem = event.isDarkThem,
                                header = state.headerValue,
                                context = event.context
                            )

                            db.insertIntoPlayingQueueTable( // todo
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.ALBUM_PREV
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = pair.first
                                )
                            )

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.PlayPause)
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ALBUM_PREV_SONG -> {
                        if (state.player.info.id == event.otherId) {
                            val index = state.player.allSong.map { it.playerSong.id }
                                .indexOf(event.songId)

                            val oldSong = try { // same prev album song
                                state.player.allSong[index]
                            } catch (_: Exception) {
                                null
                            }

                            if (oldSong == null) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return
                            }

                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(index))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val prevAlbumDef = async { db.getPrevAlbum(event.otherId) }

                            clear.await()
                            val pair = prevAlbumDef.await().groupBy { it.albumId }.map {
                                PlayingSongInfo(
                                    id = it.key,
                                    typeName = it.value[0].albumName
                                ) to it.value.map { song ->
                                    UiPlaylistSong(
                                        isPlaying = false,
                                        songId = song.songId,
                                        title = song.title,
                                        artist = song.artist,
                                        coverImage = song.coverImage,
                                        masterPlaylistUrl = song.masterPlaylistUrl,
                                        totalTime = song.totalTime
                                    )
                                }
                            }.first()

                            if (pair.second.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            val songs = pair.second.toPlayerData(
                                isDarkThem = event.isDarkThem,
                                header = state.headerValue,
                                context = event.context
                            )

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.ALBUM_PREV
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = pair.first
                                )
                            )

                            val i = songs.map {
                                it.playerSong.id
                            }.indexOf(event.songId)

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.SeekToSong(i))
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ARTIST_MIX -> {
                        if (state.player.info.typeName == ARTIST_MIX) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(0, 0))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val songDef = async { db.readAllArtistMix().first() }

                            clear.await()
                            val songs = songDef.await().toPlayerData()

                            if (songs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.ARTIST_MIX
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = PlayingSongInfo(
                                        typeName = ARTIST_MIX
                                    )
                                )
                            )

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.PlayPause)
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.ARTIST_MIX_SONG -> {
                        if (state.player.info.typeName == ARTIST_MIX) {
                            val index =
                                state.player.allSong.map { it.playerSong.id }.indexOf(event.songId)

                            val oldSong = try {
                                state.player.allSong[index]
                            } catch (_: Exception) {
                                null
                            }

                            if (oldSong == null) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return
                            }

                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(index))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val songDef = async { db.readAllArtistMix().first() }

                            clear.await()
                            val songs = songDef.await().toPlayerData()

                            if (songs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.ARTIST_MIX
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = PlayingSongInfo(
                                        typeName = ARTIST_MIX
                                    )
                                )
                            )

                            val i = songs.map {
                                it.playerSong.id
                            }.indexOf(event.songId)

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.SeekToSong(i))
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.DAILY_MIX -> {
                        if (state.player.info.typeName == DAILY_MIX) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(0, 0))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val songDef = async { db.readAllDailyMix().first() }

                            clear.await()
                            val songs = songDef.await().toPlayerData()

                            if (songs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.DAILY_MIX
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = PlayingSongInfo(
                                        typeName = DAILY_MIX
                                    )
                                )
                            )

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.PlayPause)
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.DAILY_MIX_SONG -> {
                        if (state.player.info.typeName == DAILY_MIX) {
                            val index =
                                state.player.allSong.map { it.playerSong.id }.indexOf(event.songId)

                            val oldSong = try {
                                state.player.allSong[index]
                            } catch (_: Exception) {
                                null
                            }

                            if (oldSong == null) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return
                            }

                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(index))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val songDef = async { db.readAllDailyMix().first() }

                            clear.await()
                            val songs = songDef.await().toPlayerData()

                            if (songs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.DAILY_MIX
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = PlayingSongInfo(
                                        typeName = DAILY_MIX
                                    )
                                )
                            )

                            val i = songs.map {
                                it.playerSong.id
                            }.indexOf(event.songId)

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.SeekToSong(i))
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.FAVOURITE -> {
                        Log.d("FAVOURITE", "FAVOURITE")

                        if (state.player.info.typeName == FAVOURITE) {
                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(0, 0))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val songDef = async { db.getAllFavouriteSongs().first() }

                            clear.await()
                            val songs = songDef.await().toPlayerData()

                            if (songs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.FAVOURITE
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = PlayingSongInfo(
                                        typeName = FAVOURITE
                                    )
                                )
                            )

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.PlayPause)
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    UiEvent.PlayType.FAVOURITE_SONG -> {
                        if (state.player.info.typeName == FAVOURITE) {
                            val index =
                                state.player.allSong.map { it.playerSong.id }.indexOf(event.songId)

                            val oldSong = try {
                                state.player.allSong[index]
                            } catch (_: Exception) {
                                null
                            }

                            if (oldSong == null) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return
                            }

                            viewModelScope.launch(Dispatchers.Main) {
                                player.onEvent(PlayerUiEvent.SeekToSong(index))

                                state = state.copy(
                                    player = state.player.copy(
                                        isLoading = false
                                    )
                                )
                            }

                            return
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            val clear = async { db.clearPlayingQueue() }
                            val songDef = async { db.getAllFavouriteSongs().first() }

                            clear.await()
                            val songs = songDef.await().toPlayerData()

                            if (songs.isEmpty()) {
                                onEvent(HomeRootUiEvent.SomethingWentWrong)

                                state = state.copy(
                                    player = state.player.copy(
                                        isSmallPlayer = false,
                                        isLoading = false
                                    )
                                )

                                return@launch
                            }

                            db.insertIntoPlayingQueueTable(
                                entrys = songs.map {
                                    it.playerSong
                                }.toPlayingQueueTable(),
                                songType = UiEvent.PlayType.FAVOURITE
                            )

                            state = state.copy(
                                player = state.player.copy(
                                    allSong = songs,
                                    info = PlayingSongInfo(
                                        typeName = FAVOURITE
                                    )
                                )
                            )

                            val i = songs.map {
                                it.playerSong.id
                            }.indexOf(event.songId)

                            CoroutineScope(Dispatchers.Main).launch {
                                player.onEvent(PlayerUiEvent.Stop)
                                songs.map {
                                    it.playerSong
                                }.setMediaItems()
                                player.onEvent(PlayerUiEvent.SeekToSong(i))
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    isLoading = false
                                )
                            )
                        }
                    }

                    else -> Unit
                }
            }

            is HomeRootUiEvent.PlayerUiEvent -> {
                when (event) {
                    HomeRootUiEvent.PlayerUiEvent.SmallPlayerClick -> {
                        state = if (!state.player.isPlayerOpen) state.copy(
                            player = state.player.copy(
                                isPlayerOpen = true
                            )
                        ) else {
                            state.copy(
                                player = state.player.copy(
                                    isPlayerOpen = false
                                )
                            )
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.CancelPlay -> {
                        state = state.copy(
                            player = Player()
                        )

                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.Stop)
                        }

                        viewModelScope.launch(Dispatchers.IO) {
                            db.clearPlayingQueue()

                            delay(300)

                            state = state.copy(
                                anchors = AnchoredDraggableState(
                                    initialValue = DragAnchors.START,
                                    positionalThreshold = { it * 0.5f },
                                    velocityThreshold = { 1f },
                                    animationSpec = tween()
                                ).apply {
                                    updateAnchors(
                                        DraggableAnchors {
                                            DragAnchors.START at 0f
                                            DragAnchors.END at 250f
                                        }
                                    )
                                }
                            )
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.Backward -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.SeekToPrev)
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.Forward -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.SeekToNext)
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.PlayPause -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.PlayPause)
                        }
                    }

                    is HomeRootUiEvent.PlayerUiEvent.SeekTo -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.SeekTo((state.player.playingSong.totalInMili * event.index / 100f).toLong()))
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.SeekToPrev -> {
                        viewModelScope.launch(Dispatchers.Main) {

                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.SeekToNext -> {
                        viewModelScope.launch(Dispatchers.Main) {

                        }
                    }

                    is HomeRootUiEvent.PlayerUiEvent.SelectedSongChange -> {
                        viewModelScope.launch(Dispatchers.Main) {

                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.Stop -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.Stop)
                        }
                    }

                    is HomeRootUiEvent.PlayerUiEvent.UpdateProgress -> {
                        viewModelScope.launch(Dispatchers.Main) {

                        }
                    }
                }
            }

            is HomeRootUiEvent.UpdateNav -> {
                state = when (event.screens) {
                    Screens.Home -> state.copy(
                        nav = Nav.HOME
                    )

                    Screens.Library -> state.copy(
                        nav = Nav.LIB
                    )

                    else -> state.copy(
                        nav = Nav.NON
                    )
                }
            }

            is HomeRootUiEvent.BottomNavClick -> {
                when (event.bottomNav) {
                    HomeScreenBottomNavigation.HOME_SCREEN -> {
                        if (state.nav != Nav.HOME) {
                            viewModelScope.launch(Dispatchers.IO) {
                                _uiEvent.send(UiEvent.Navigate(Screens.Home.route))
                            }
                        }
                    }

                    HomeScreenBottomNavigation.LIBRARY_SCREEN -> {
                        if (state.nav != Nav.LIB) {
                            viewModelScope.launch(Dispatchers.IO) {
                                _uiEvent.send(UiEvent.Navigate(Screens.Library.route))
                            }
                        }
                    }
                }
            }

            HomeRootUiEvent.LogOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    storeSignInState(SignInStatus.AUTH, ds)

                    delay(800)

                    db.removeAllTable()
                }
            }
        }
    }

    private fun PlayerSong.setMediaItem(uri: String = "") {
        val item = MediaItem.Builder()
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .setMimeType(MimeTypes.APPLICATION_ID3)
            .setUri(masterPlaylist)
            .setLiveConfiguration(
                MediaItem
                    .LiveConfiguration.Builder()
                    .setMaxPlaybackSpeed(1.02f)
                    .build()
            ).setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(this.title)
                    .setArtist(this.artist.toString().trimStart('[').trimEnd(']'))
                    .setAlbumTitle(this.album)
                    .setArtworkUri(Uri.parse(uri))
                    .build()
            )
            .setMediaId(this.id.toString())
            .build()

        player.addOneMediaItem(item)
    }

    private fun List<PlayerSong>.setMediaItems() {
        this.map {
            MediaItem.Builder()
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .setMimeType(MimeTypes.APPLICATION_ID3)
                .setUri(it.masterPlaylist)
                .setLiveConfiguration(
                    MediaItem
                        .LiveConfiguration.Builder()
                        .setMaxPlaybackSpeed(1.02f)
                        .build()
                ).setMediaMetadata(
                    MediaMetadata.Builder()
                        .setDisplayTitle(it.title)
                        .setArtist(it.artist.toString().trimStart('[').trimEnd(']'))
                        .setAlbumTitle(it.album)
                        .setArtworkUri(Uri.parse(it.url))
                        .build()
                )
                .setMediaId(it.id.toString())
                .build()
        }.let {
            player.addMultipleMediaItem(it)
        }
    }

    private fun millisecondsToMinutesAndSeconds(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000.0
        val minutes = (totalSeconds / 60).toLong()
        val seconds = (totalSeconds % 60).toLong()

        return "$minutes.$seconds"
    }

    private suspend fun getArtistJob(songId: Long) = viewModelScope.launch(Dispatchers.IO) {
        val result = api.getArtistOnSongId(songId).toViewArtist()

        if (result.isEmpty()) return@launch

        state = state.copy(
            player = state.player.copy(
                playingSongArtist = result
            )
        )
    }
}