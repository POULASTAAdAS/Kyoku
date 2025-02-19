package com.poulastaa.main.presentation.main

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toOffset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.model.DtoCoreScreens
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.toUiUser
import com.poulastaa.main.domain.model.AppDrawerScreen
import com.poulastaa.main.domain.model.AppNavigationBottomBarScreen
import com.poulastaa.main.domain.model.AppNavigationDrawerState
import com.poulastaa.main.domain.model.AppNavigationRailScreen
import com.poulastaa.main.domain.model.AppNavigationRailState
import com.poulastaa.main.domain.model.isOpened
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val ds: DatastoreRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state = _state
        .onStart {
            loadUser()
            loadGreeting()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MainUiState()
        )

    private val _uiEvent = Channel<MainUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: MainUiAction) {
        when (action) {
            MainUiAction.ToggleDrawer -> {
                _state.update {
                    it.copy(
                        navigationDrawerState = if (_state.value.navigationDrawerState.isOpened()) AppNavigationDrawerState.CLOSED
                        else AppNavigationDrawerState.OPENED
                    )
                }
            }

            is MainUiAction.NavigateToDrawerScreen -> {
                viewModelScope.launch {
                    if (action.screen == AppDrawerScreen.THEME) startThemChangeTransition(action.offset)
                    else _state.update {
                        it.copy(
                            navigationDrawerState = AppNavigationDrawerState.CLOSED
                        )
                    }

                    _uiEvent.send(MainUiEvent.Navigate(action.screen.toDtoCoreScreen()))
                }
            }

            MainUiAction.ToggleNavigationRail -> {
                _state.update {
                    it.copy(
                        navigationRailState = if (it.navigationRailState.isOpened()) AppNavigationRailState.CLOSED
                        else AppNavigationRailState.OPENED
                    )
                }
            }

            is MainUiAction.NavigateToNavigationRailScreen -> {
                if (_state.value.navigationRailScreen == action.screen) return

                viewModelScope.launch {
                    if (action.screen == AppNavigationRailScreen.THEME)
                        startThemChangeTransition(action.offset)
                    else _state.update {
                        it.copy(
                            navigationRailScreen = action.screen,
                            navigationBottomBarScreen = action.screen.toAppBottomBarScreen()
                                ?: it.navigationBottomBarScreen,
                            navigationRailState = AppNavigationRailState.CLOSED
                        )
                    }

                    when (action.screen) {
                        AppNavigationRailScreen.HOME -> _uiEvent.send(
                            MainUiEvent.NavigateMain(
                                ScreensCore.Home
                            )
                        )

                        AppNavigationRailScreen.LIBRARY -> _uiEvent.send(
                            MainUiEvent.NavigateMain(
                                ScreensCore.Library
                            )
                        )

                        else -> _uiEvent.send(
                            MainUiEvent.Navigate(
                                action.screen.toDtoCoreScreen()
                            )
                        )
                    }
                }
            }

            is MainUiAction.NavigateBottomBarScreen -> {
                if (_state.value.navigationBottomBarScreen == action.screen) return
                _state.update {
                    it.copy(
                        navigationBottomBarScreen = action.screen,
                        navigationRailScreen = action.screen.toAppNavigationRailScreen()
                    )
                }

                viewModelScope.launch {
                    _uiEvent.send(MainUiEvent.NavigateMain(action.screen.toMainScreen()))
                }
            }

            MainUiAction.ResetRevelAnimation -> {
                _state.update {
                    it.copy(
                        offset = IntOffset(0, 0).toOffset()
                    )
                }
            }
        }
    }

    fun updateMainScreenNavigationUiAfterNavigation(payload: String) {
        val bottom = _state.value.navigationBottomBarScreen
        val rail = _state.value.navigationRailScreen

        val screen = payload.split('.').last().uppercase()
        val newBottom = screen.toNewBottom()
        screen.toNewRail()?.let {
            if (it == rail) return@let
            _state.update { state ->
                state.copy(
                    navigationRailScreen = it
                )
            }
        }

        if (newBottom == bottom) return
        _state.update {
            it.copy(
                navigationBottomBarScreen = newBottom
            )
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = ds.readLocalUser()
            _state.update {
                it.copy(
                    user = user.toUiUser()
                )
            }
        }
    }

    private fun loadGreeting() {
        val now = LocalTime.now()

        val greetings = when {
            now >= LocalTime.of(7, 0) && now <= LocalTime.of(11, 59) -> "Good Morning"
            now >= LocalTime.of(12, 0) && now <= LocalTime.of(17, 59) -> "Good Afternoon"
            now >= LocalTime.of(18, 0) && now <= LocalTime.of(23, 59) -> "Good Evening"
            now >= LocalTime.MIDNIGHT && now <= LocalTime.of(3, 59) -> "Night Owl"
            now >= LocalTime.of(4, 0) && now <= LocalTime.of(6, 59) -> "Early Bird"
            else -> "Hello"
        }

        _state.update {
            it.copy(
                greetings = greetings
            )
        }
    }

    private suspend fun startThemChangeTransition(offset: Offset?) {
        offset?.let {
            _state.update {
                it.copy(
                    offset = offset
                )
            }
        }
        // Delay for the animation to complete
        delay((_state.value.themChangeAnimationTime / 1.3).toDuration(DurationUnit.MILLISECONDS))
    }

    private fun AppNavigationBottomBarScreen.toAppNavigationRailScreen() = when (this) {
        AppNavigationBottomBarScreen.HOME -> AppNavigationRailScreen.HOME
        AppNavigationBottomBarScreen.LIBRARY -> AppNavigationRailScreen.LIBRARY
    }

    private fun AppNavigationRailScreen.toAppBottomBarScreen() =
        if (this == AppNavigationRailScreen.HOME) AppNavigationBottomBarScreen.HOME
        else if (this == AppNavigationRailScreen.LIBRARY) AppNavigationBottomBarScreen.LIBRARY
        else null

    private fun AppDrawerScreen.toDtoCoreScreen() = when (this) {
        AppDrawerScreen.PROFILE -> DtoCoreScreens.Profile
        AppDrawerScreen.HISTORY -> DtoCoreScreens.History
        AppDrawerScreen.SETTINGS -> DtoCoreScreens.Settings
        AppDrawerScreen.THEME -> DtoCoreScreens.ToggleTheme
    }

    private fun AppNavigationRailScreen.toDtoCoreScreen() = when (this) {
        AppNavigationRailScreen.HISTORY -> DtoCoreScreens.History
        AppNavigationRailScreen.SETTINGS -> DtoCoreScreens.Settings
        AppNavigationRailScreen.THEME -> DtoCoreScreens.ToggleTheme

        AppNavigationRailScreen.PROFILE -> DtoCoreScreens.Profile
        else -> throw IllegalArgumentException("Home and Library should not occur on file MainViewModel")
    }

    private fun AppNavigationBottomBarScreen.toMainScreen() = when (this) {
        AppNavigationBottomBarScreen.HOME -> ScreensCore.Home
        AppNavigationBottomBarScreen.LIBRARY -> ScreensCore.Library
    }

    private fun String.toNewBottom() =
        if (this == AppNavigationBottomBarScreen.HOME.name) AppNavigationBottomBarScreen.HOME
        else AppNavigationBottomBarScreen.LIBRARY

    private fun String.toNewRail() =
        if (this == AppNavigationRailScreen.HOME.name) AppNavigationRailScreen.HOME
        else if (this == AppNavigationRailScreen.LIBRARY.name) AppNavigationRailScreen.LIBRARY
        else null
}