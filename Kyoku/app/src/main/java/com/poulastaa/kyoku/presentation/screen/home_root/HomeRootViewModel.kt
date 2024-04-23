package com.poulastaa.kyoku.presentation.screen.home_root

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.kyoku.data.model.SignInStatus
import com.poulastaa.kyoku.data.model.api.auth.AuthType
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiEvent
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeRootUiState
import com.poulastaa.kyoku.data.model.home_nav_drawer.HomeScreenBottomNavigation
import com.poulastaa.kyoku.data.model.home_nav_drawer.Nav
import com.poulastaa.kyoku.data.model.screens.auth.UiEvent
import com.poulastaa.kyoku.data.model.screens.home.SongType
import com.poulastaa.kyoku.data.repository.DatabaseRepositoryImpl
import com.poulastaa.kyoku.domain.repository.DataStoreOperation
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import com.poulastaa.kyoku.navigation.Screens
import com.poulastaa.kyoku.utils.storeSignInState
import com.poulastaa.kyoku.utils.toPlayerData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeRootViewModel @Inject constructor(
    private val ds: DataStoreOperation,
    private val db: DatabaseRepositoryImpl,
    private val api: ServiceRepository
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

            if (authType == AuthType.UN_AUTH.name) {
                // TODO: handle quick auth
            }

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

    init {
        readAccessToken()
        readAuthType()
        readUserName()
        readProfilePicUrl()
        setHomeTopBarTitle()
    }

    init {
        viewModelScope.launch {
            db.readAllFromPlayingQueue().collect {
                if (it.isNotEmpty()) state = state.copy(
                    playerData = it.toPlayerData()
                )
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
                        isPlayer = true,
                        isPlayerLoading = true
                    )

                    when (event.songType) {
                        SongType.HISTORY_SONG -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                if (db.checkIfAlreadyInPlayingQueue(event.id) == null) {
                                    val song = api.getSongOnId(event.id)

                                    if (song.id == -1L) {
                                        onEvent(HomeRootUiEvent.SomethingWentWrong)

                                        state = state.copy(
                                            isPlayer = false,
                                            isPlayerLoading = false
                                        )

                                        return@launch
                                    }

                                    db.insertIntoPlayingQueueTable(song)
                                }
                            }
                        }

                        SongType.ARTIST_SONG -> {
                            viewModelScope.launch(Dispatchers.IO) {
                                val song = api.getSongOnId(event.id)

                                if (song.id == -1L) {
                                    onEvent(HomeRootUiEvent.SomethingWentWrong)

                                    state = state.copy(
                                        isPlayer = false,
                                        isPlayerLoading = false
                                    )

                                    return@launch
                                }

                                db.insertIntoPlayingQueueTable(song)
                            }
                        }

                        SongType.ALBUM_SONG -> {

                        }

                        SongType.PLAYLIST_SONG -> {

                        }

                        SongType.API_CALL -> {

                        }
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

            is HomeRootUiEvent.Update -> {
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

            HomeRootUiEvent.LogOut -> {
                viewModelScope.launch(Dispatchers.IO) {
                    storeSignInState(SignInStatus.AUTH, ds)

                    delay(800)

                    db.removeAllTable()
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
        }
    }
}