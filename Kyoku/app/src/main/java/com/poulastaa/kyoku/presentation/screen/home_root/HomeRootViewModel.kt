package com.poulastaa.kyoku.presentation.screen.home_root

import android.graphics.Color.parseColor
import android.net.Uri
import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeScreenBottomNavigation
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.data.model.home_nav_drawer.Player
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.data.model.screens.player.DragAnchors
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.data.model.screens.player.PlayerUiEvent
import com.poulastaa.kyoku.data.model.screens.player.PlayerUiState
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.player.service.AudioServiceHandler
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.BitmapConverter
import com.poulastaa.kyoku.utils.ColorType
import com.poulastaa.kyoku.utils.PaletteGenerator
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toPlayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

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

                    is PlayerUiState.CurrentPlayingIndex -> {
                        Log.d("update CurrentPlayingIndex", event.index.toString())
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
                        Log.d("update Progress", event.value.toString())

                        state = state.copy(
                            player = state.player.copy(
                                progress = calculateProgress(event.value),
                                playingSong = state.player.playingSong.copy(
                                    currentInMin = millisecondsToMinutesAndSeconds(event.value)
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
                if (event.route == Screens.Player.route) {
                    state = state.copy(
                        player = state.player.copy(
                            isSmallPlayer = true,
                            isLoading = true
                        )
                    )

                    when (event.songType) {
                        SongType.HISTORY_SONG -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                if (db.checkIfAlreadyInPlayingQueue(event.id) == null) {
                                    val song = api.getSongOnId(event.id)

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
                                            event.songType
                                        )
                                    }.await()

                                    val list = db.readAllFromPlayingQueue().first().toPlayerData()

                                    val image =
                                        BitmapConverter.decodeToBitmap(list[0].url)

                                    if (image != null) {
                                        val colors = PaletteGenerator.extractColorFromBitMap(image)

                                        val variant =
                                            Color(parseColor(colors[ColorType.LIGHT_VIBRANT]))
                                        val darkVariant =
                                            Color(parseColor(colors[ColorType.DARK_VIBRANT]))

                                        if (variant != darkVariant)
                                            state = state.copy(
                                                player = state.player.copy(
                                                    colors = listOf(
                                                        variant,
                                                        darkVariant,
                                                    )
                                                )
                                            )
                                    }

                                    CoroutineScope(Dispatchers.Main).launch {
                                        player.onEvent(PlayerUiEvent.Stop)
                                        list[0].setMediaItem(song.coverImage)
                                        player.onEvent(PlayerUiEvent.PlayPause)
                                    }

                                    state = state.copy(
                                        player = state.player.copy(
                                            allSong = list,
                                            playingSong = list[0]
                                        )
                                    )
                                } else {
                                    viewModelScope.launch(Dispatchers.Main) {
                                        player.onEvent(PlayerUiEvent.SeekTo(0))
                                    }
                                }
                            }
                        }

                        SongType.ARTIST_SONG -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                if (db.checkIfAlreadyInPlayingQueue(event.id) == null) {
                                    val song = api.getSongOnId(event.id)

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
                                            event.songType
                                        )
                                    }.await()

                                    val list = db.readAllFromPlayingQueue().first().toPlayerData()

                                    val image =
                                        BitmapConverter.decodeToBitmap(list[0].url)

                                    if (image != null) {
                                        val colors = PaletteGenerator.extractColorFromBitMap(image)

                                        val variant =
                                            Color(parseColor(colors[ColorType.LIGHT_VIBRANT]))
                                        val darkVariant =
                                            Color(parseColor(colors[ColorType.DARK_VIBRANT]))

                                        if (variant != darkVariant)
                                            state = state.copy(
                                                player = state.player.copy(
                                                    colors = listOf(
                                                        variant,
                                                        darkVariant,
                                                    )
                                                )
                                            )
                                    }

                                    CoroutineScope(Dispatchers.Main).launch {
                                        player.onEvent(PlayerUiEvent.Stop)
                                        list[0].setMediaItem(song.coverImage)
                                        player.onEvent(PlayerUiEvent.PlayPause)
                                    }

                                    state = state.copy(
                                        player = state.player.copy(
                                            allSong = list,
                                            playingSong = list[0]
                                        )
                                    )
                                } else {
                                    viewModelScope.launch(Dispatchers.Main) {
                                        player.onEvent(PlayerUiEvent.SeekTo(0))
                                    }
                                }
                            }
                        }

                        SongType.ALBUM_SONG -> {
                            viewModelScope.launch(Dispatchers.IO) {

                            }
                        }

                        SongType.PLAYLIST_SONG -> {
                            viewModelScope.launch(Dispatchers.IO) {

                            }
                        }

                        SongType.API_CALL -> {
                            viewModelScope.launch(Dispatchers.IO) {

                            }
                        }
                    }.let {
                        state = state.copy(
                            player = state.player.copy(
                                isLoading = false
                            )
                        )
                    }

                } else viewModelScope.launch(Dispatchers.IO) {
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
                            player.onEvent(PlayerUiEvent.Backward)
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.Forward -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.Forward)
                        }
                    }

                    HomeRootUiEvent.PlayerUiEvent.PlayPause -> {
                        viewModelScope.launch(Dispatchers.Main) {
                            player.onEvent(PlayerUiEvent.PlayPause)
                        }
                    }

                    is HomeRootUiEvent.PlayerUiEvent.SeekTo -> {
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

    private fun calculateProgress(value: Long) =
        try {
            if (value > 0) {
                val v =
                    (value.toFloat() / state.player.playingSong.totalInMili)


                Log.d("value", "$value , $v")

                v
            } else 0f
        } catch (_: Exception) {
            0f
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
                    .setDisplayTitle(title)
                    .setArtist(artist.toString().trimStart('[').trimEnd(']'))
                    .setAlbumTitle(album)
                    .setArtworkUri(Uri.parse(uri))
                    .build()
            )
            .setMediaId(id.toString())
            .build()

        player.addOneMediaItem(item)
    }

    private fun millisecondsToMinutesAndSeconds(milliseconds: Long): String {
        val totalSeconds = milliseconds / 1000.0
        val minutes = (totalSeconds / 60).toLong()
        val seconds = (totalSeconds % 60).toLong()

        return "$minutes.$seconds"
    }

}