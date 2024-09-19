package com.poulastaa.play.presentation.root_drawer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.model.PlayerEvent
import com.poulastaa.core.domain.model.PlayerState
import com.poulastaa.core.domain.repository.player.KyokuPlayer
import com.poulastaa.core.domain.repository.player.PlayerRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.ui.UiText
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.domain.SyncLibraryScheduler
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeAddToPlaylistUiState
import com.poulastaa.play.presentation.song_artist.toSongArtistUiArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class RootDrawerViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val syncScheduler: SyncLibraryScheduler,
    private val repo: PlayerRepository,
    private val player: KyokuPlayer,
) : ViewModel() {
    var state by mutableStateOf(RootDrawerUiState())
        private set

    private var loadInfoJob: Job? = null
    private var loadSongsJob: Job? = null

    init {
        viewModelScope.launch {
            syncScheduler.scheduleSync(30.minutes)
        }

        viewModelScope.launch {
            val savedScreenStringDef = async {
                ds.readSaveScreen().first()
            }
            val userDef = async { ds.readLocalUser() }
            val savedScreen = savedScreenStringDef.await()
            val user = userDef.await()

            state = state.copy(
                saveScreen = savedScreen.toSaveScreen(),
                startDestination = savedScreen.toDrawerScreen().route,
                username = user.name,
                profilePicUrl = user.profilePic
            )
        }

        viewModelScope.launch {
            player.playerUiState.collectLatest { playerState ->
                when (playerState) {
                    is PlayerState.ProgressBar -> {
                        state = state.copy(
                            player = state.player.copy(
                                info = state.player.info.copy(
                                    progress = playerState.value
                                )
                            )
                        )
                    }

                    is PlayerState.Progress -> {
                        state = state.copy(
                            player = state.player.copy(
                                info = state.player.info.copy(
                                    currentProgress = playerState.value
                                )
                            )
                        )
                    }

                    is PlayerState.Ready -> {
                        state = state.copy(
                            player = state.player.copy(
                                info = state.player.info.copy(
                                    endTime = playerState.totalTime
                                )
                            )
                        )
                    }

                    is PlayerState.Playing -> {
                        state = state.copy(
                            player = state.player.copy(
                                info = state.player.info.copy(
                                    isPlaying = playerState.isPlaying
                                )
                            )
                        )
                    }

                    is PlayerState.CurrentlyPlaying -> {
                        state = state.copy(
                            player = state.player.copy(
                                info = state.player.info.copy(
                                    hasPrev = playerState.hasPrev,
                                    hasNext = playerState.hasNext
                                )
                            )
                        )

                        state = state.copy(
                            player = state.player.copy(
                                queue = state.player.queue.map { uiSong ->
                                    if (uiSong.songId == playerState.songId) uiSong.copy(isPlaying = true)
                                    else uiSong.copy(isPlaying = false)
                                }
                            )
                        )

                        state.player.queue
                            .firstOrNull { it.songId == playerState.songId }
                            ?.let { song ->
                                state = state.copy(
                                    player = state.player.copy(
                                        info = state.player.info.copy(
                                            currentPlayingIndex = song.index.dec(),
                                        )
                                    )
                                )
                            }
                    }

                    else -> Unit
                }
            }
        }

        readHeader()
//        loadPlayingData()
    }

    private val _uiEvent = Channel<RootDrawerUiAction>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var updateSaveScreenJob: Job? = null

    fun onEvent(event: RootDrawerUiEvent) {
        when (event) {
            is RootDrawerUiEvent.Navigate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _uiEvent.send(
                        RootDrawerUiAction.Navigate(event.screen)
                    )
                }
            }

            is RootDrawerUiEvent.SaveScreenToggle -> {
                if (state.startDestination != event.screen.name.toDrawScreenRoute()) {
                    state = state.copy(
                        startDestination = when (event.screen) {
                            SaveScreen.HOME -> {
                                updateSaveScreenJob?.cancel()
                                updateSaveScreenJob = updateSaveScreen(SaveScreen.HOME)

                                DrawerScreen.Home.route
                            }

                            SaveScreen.LIBRARY -> {
                                updateSaveScreenJob?.cancel()
                                updateSaveScreenJob = updateSaveScreen(SaveScreen.LIBRARY)

                                DrawerScreen.Library.route
                            }
                        },
                        saveScreen = event.screen
                    )
                }
            }

            is RootDrawerUiEvent.AddSongToPlaylist -> {
                state = state.copy(
                    addToPlaylistUiState = state.addToPlaylistUiState.copy(
                        isOpen = true,
                        songId = event.id
                    )
                )
            }

            RootDrawerUiEvent.OnAddSongToPlaylistCancel -> {
                state = state.copy(
                    addToPlaylistUiState = HomeAddToPlaylistUiState()
                )
            }

            is RootDrawerUiEvent.View -> {
                state = state.copy(
                    viewUiState = HomeViewUiState(
                        isOpen = true,
                        songId = event.id,
                        type = event.type,
                    ),
                    addToPlaylistUiState = HomeAddToPlaylistUiState(),
                    createPlaylistUiState = CreatePlaylistViewUiState()
                )
            }

            RootDrawerUiEvent.OnViewCancel -> {
                state = state.copy(
                    viewUiState = HomeViewUiState()
                )
            }

            is RootDrawerUiEvent.OnExploreArtistOpen -> {
                state = state.copy(
                    exploreArtistUiState = state.exploreArtistUiState.copy(
                        isOpen = true,
                        artistId = event.id
                    ),
                    addToPlaylistUiState = HomeAddToPlaylistUiState(),
                    viewUiState = HomeViewUiState(),
                    newAlbumUiState = NewAlbumViewUiState()
                )
            }

            RootDrawerUiEvent.OnExploreArtistCancel -> {
                state = state.copy(
                    exploreArtistUiState = ExploreArtistUiState()
                )
            }

            is RootDrawerUiEvent.NewAlbum -> {
                state = state.copy(
                    newAlbumUiState = state.newAlbumUiState.copy(
                        isOpen = true
                    ),
                    addToPlaylistUiState = HomeAddToPlaylistUiState(),
                    viewUiState = HomeViewUiState(),
                    exploreArtistUiState = ExploreArtistUiState(),
                    newArtisUiState = NewArtistViewUiState()
                )
            }

            RootDrawerUiEvent.NewAlbumCancel -> {
                state = state.copy(
                    newAlbumUiState = state.newAlbumUiState.copy(
                        isOpen = false
                    )
                )
            }

            is RootDrawerUiEvent.NewArtist -> {
                state = state.copy(
                    newArtisUiState = state.newArtisUiState.copy(
                        isOpen = true
                    ),
                    addToPlaylistUiState = HomeAddToPlaylistUiState(),
                    viewUiState = HomeViewUiState(),
                    exploreArtistUiState = ExploreArtistUiState(),
                    newAlbumUiState = NewAlbumViewUiState()
                )
            }

            RootDrawerUiEvent.NewArtistCancel -> {
                state = state.copy(
                    newArtisUiState = state.newArtisUiState.copy(
                        isOpen = false
                    )
                )
            }

            is RootDrawerUiEvent.CreatePlaylist -> {
                state = state.copy(
                    createPlaylistUiState = state.createPlaylistUiState.copy(
                        isOpen = true,
                        playlistId = event.playlistId
                    ),
                    addToPlaylistUiState = HomeAddToPlaylistUiState(),
                    exploreArtistUiState = ExploreArtistUiState(),
                    newAlbumUiState = NewAlbumViewUiState(),
                    newArtisUiState = NewArtistViewUiState()
                )
            }

            RootDrawerUiEvent.CreatePlaylistCancel -> {
                state = state.copy(
                    createPlaylistUiState = CreatePlaylistViewUiState(),
                )
            }

            is RootDrawerUiEvent.OnViewSongArtists -> {
                state = state.copy(
                    viewSongArtistSongId = event.songId
                )
            }

            RootDrawerUiEvent.OnViewSongArtistsCancel -> {
                state = state.copy(
                    viewSongArtistSongId = -1
                )
            }

            is RootDrawerUiEvent.PlayOperation -> {
                when (event) {
                    is RootDrawerUiEvent.PlayOperation.PlaySaved -> {
                        state = state.copy(
                            player = state.player.copy(
                                isData = false,
                                loadingState = DataLoadingState.LOADING
                            )
                        )

                        viewModelScope.launch {
                            when (val result = repo.loadData(event.id, event.type)) {
                                is Result.Error -> {
                                    when (result.error) {
                                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                                            RootDrawerUiAction.EmitToast(
                                                UiText.StringResource(
                                                    R.string.error_no_internet
                                                )
                                            )
                                        )

                                        else -> _uiEvent.send(
                                            RootDrawerUiAction.EmitToast(
                                                UiText.StringResource(
                                                    R.string.error_something_went_wrong
                                                )
                                            )
                                        )
                                    }
                                }

                                is Result.Success -> loadPlayingData()
                            }
                        }
                    }

                    is RootDrawerUiEvent.PlayOperation.ShuffleSaved -> {
                        state = state.copy(
                            player = state.player.copy(
                                isData = false,
                                loadingState = DataLoadingState.LOADING
                            )
                        )

                        viewModelScope.launch {
                            when (val result = repo.loadData(event.id, event.type, true)) {
                                is Result.Error -> {
                                    when (result.error) {
                                        DataError.Network.NO_INTERNET -> _uiEvent.send(
                                            RootDrawerUiAction.EmitToast(
                                                UiText.StringResource(
                                                    R.string.error_no_internet
                                                )
                                            )
                                        )

                                        else -> _uiEvent.send(
                                            RootDrawerUiAction.EmitToast(
                                                UiText.StringResource(
                                                    R.string.error_something_went_wrong
                                                )
                                            )
                                        )
                                    }
                                }

                                is Result.Success -> loadPlayingData()
                            }
                        }
                    }
                }
            }

            else -> Unit
        }
    }

    fun onPlayerEvent(event: PlayerUiEvent) {
        when (event) {
            PlayerUiEvent.OnPlayerExtendClick -> {
                state = state.copy(
                    player = state.player.copy(
                        isPlayerExtended = true
                    )
                )
            }

            PlayerUiEvent.OnPlayerShrinkClick -> {
                state = state.copy(
                    player = state.player.copy(
                        isPlayerExtended = false
                    )
                )
            }

            is PlayerUiEvent.PlayBackController -> {
                when (event) {
                    PlayerUiEvent.PlayBackController.OnPlayPrevClick -> player.onEvent(PlayerEvent.PlayPrev)
                    PlayerUiEvent.PlayBackController.OnPlayNextClick -> player.onEvent(PlayerEvent.PlayNext)
                    is PlayerUiEvent.PlayBackController.OnPlayPause -> player.onEvent(PlayerEvent.PlayPause)
                    is PlayerUiEvent.PlayBackController.SeekTo -> player.onEvent(
                        PlayerEvent.SeekTo(
                            event.pos
                        )
                    )

                    is PlayerUiEvent.PlayBackController.OnQueueSongClick -> {
                        val songIndex = state.player.queue.indexOfFirst { it.songId == event.songId }
                        player.onEvent(PlayerEvent.SeekToSong(songIndex))
                    }
                }
            }

            is PlayerUiEvent.GetSongInfo -> {
                if (state.player.info.artist.songId == event.songId) return

                state = state.copy(
                    player = state.player.copy(
                        info = state.player.info.copy(
                            artist = state.player.info.artist.copy(
                                songId = event.songId,
                                loadingState = DataLoadingState.LOADING,
                            )
                        )
                    )
                )

                viewModelScope.launch {
                    when (val result = repo.getArtistOnSongId(event.songId)) {
                        is Result.Error -> {
                            when (result.error) {
                                DataError.Network.NO_INTERNET -> _uiEvent.send(
                                    RootDrawerUiAction.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_no_internet
                                        )
                                    )
                                )

                                else -> _uiEvent.send(
                                    RootDrawerUiAction.EmitToast(
                                        UiText.StringResource(
                                            R.string.error_something_went_wrong
                                        )
                                    )
                                )
                            }

                            state = state.copy(
                                player = state.player.copy(
                                    info = state.player.info.copy(
                                        artist = state.player.info.artist.copy(
                                            loadingState = DataLoadingState.ERROR
                                        )
                                    )
                                )
                            )
                        }

                        is Result.Success -> {
                            state = state.copy(
                                player = state.player.copy(
                                    info = state.player.info.copy(
                                        artist = state.player.info.artist.copy(
                                            songId = if (result.data.isEmpty()) -1 else event.songId,
                                            loadingState = if (result.data.isEmpty()) DataLoadingState.ERROR else DataLoadingState.LOADED,
                                            artist = result.data.map { it.toSongArtistUiArtist() }
                                        )
                                    )
                                )
                            )
                        }
                    }
                }
            }

            PlayerUiEvent.ClosePlayer -> {
                state = state.copy(
                    player = PlayerUiState()
                )
            }

            else -> Unit
        }
    }

    private fun updateSaveScreen(screen: SaveScreen) = viewModelScope.launch {
        ds.storeSaveScreen(screen.name)
    }

    private fun loadPlayingData() {
        loadInfoJob?.cancel()
        loadSongsJob?.cancel()
        loadSongsJob = loadSongs()
        loadInfoJob = loadInfo()
    }

    private fun loadSongs() = viewModelScope.launch {
        val data = repo.getSongs().first()

        player.addMediaItem(data)
        player.onEvent(PlayerEvent.PlayPause)

        state = state.copy(
            player = state.player.copy(
                isData = data.isNotEmpty(),
                loadingState = DataLoadingState.LOADED,
                queue = data.map { it.toPlayerUiSong() }
            )
        )

//        repo.getSongs().collectLatest { payload ->
//            state = state.copy(
//                player = state.player.copy(
//                    isData = payload.isNotEmpty(),
//                    loadingState = DataLoadingState.LOADED,
//                    queue = payload.map { it.toPlayerUiSong() }
//                )
//            )
//        }
    }

    private fun loadInfo() = viewModelScope.launch {
        val info = repo.getInfo().first()

        state = state.copy(
            player = state.player.copy(
                info = info.toPlayerUiInfo(0)
            )
        )

//        repo.getInfo().collectLatest { payload ->
//            state = state.copy(
//                player = state.player.copy(
//                    info = payload.toPlayerUiInfo(0)
//                )
//            )
//        }
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