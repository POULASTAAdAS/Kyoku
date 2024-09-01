package com.poulastaa.play.presentation.root_drawer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.domain.SyncLibraryScheduler
import com.poulastaa.play.presentation.root_drawer.home.HomeAddToPlaylistUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class RootDrawerViewModel @Inject constructor(
    private val ds: DataStoreRepository,
    private val syncScheduler: SyncLibraryScheduler
) : ViewModel() {
    var state by mutableStateOf(RootDrawerUiState())
        private set

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
                    newAlbumUiState = NewAlbumUiState()
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
                    exploreArtistUiState = ExploreArtistUiState()
                )
            }

            RootDrawerUiEvent.NewAlbumCancel -> {
                state = state.copy(
                    newAlbumUiState = state.newAlbumUiState.copy(
                        isOpen = false
                    )
                )
            }

            else -> Unit
        }
    }


    private fun updateSaveScreen(screen: SaveScreen) = viewModelScope.launch {
        ds.storeSaveScreen(screen.name)
    }
}