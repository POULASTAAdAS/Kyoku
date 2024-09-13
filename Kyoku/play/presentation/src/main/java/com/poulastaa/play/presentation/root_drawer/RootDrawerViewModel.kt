package com.poulastaa.play.presentation.root_drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.core.domain.repository.player.PlayerRepository
import com.poulastaa.play.domain.DataLoadingState
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.domain.SyncLibraryScheduler
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeAddToPlaylistUiState
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
    private val repo: PlayerRepository
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

        readHeader()
        loadPlayingData()
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
                    viewUiState = HomeViewUiState(),
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

            is RootDrawerUiEvent.PlayOperation -> {
                when (event) {
                    is RootDrawerUiEvent.PlayOperation.ViewPlayAll -> {
                        state = state.copy(
                            player = PlayerUiState(
                                isData = false,
                                loadingState = DataLoadingState.LOADING
                            )
                        )

                        viewModelScope.launch {
                            async { repo.loadData(event.id, event.type) }.await()
                            loadPlayingData()
                        }
                    }

                    is RootDrawerUiEvent.PlayOperation.ViewShuffle -> {

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

            PlayerUiEvent.PlayBackController.OnPlayNextClick -> {

            }

            is PlayerUiEvent.PlayBackController.OnPlayPause -> {

            }

            PlayerUiEvent.PlayBackController.OnPlayPrevClick -> {

            }

            is PlayerUiEvent.PlayBackController.SeekTo -> {

            }
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
        repo.getSongs().collectLatest { payload ->
            state = state.copy(
                player = state.player.copy(
                    isData = payload.isNotEmpty(),
                    loadingState = DataLoadingState.LOADED,
                    queue = payload.map { it.toPlayerUiSong() }
                )
            )
        }
    }

    private fun loadInfo() = viewModelScope.launch {
        repo.getInfo().collectLatest { payload ->
            state = state.copy(
                player = state.player.copy(
                    info = payload.toPlayerUiInfo(0)
                )
            )
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