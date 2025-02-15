package com.poulastaa.main.presentation.main

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    private val ds: DatastoreRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state = _state
        .onStart {
            loadUser()
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
                _state.value = _state.value.copy(
                    navigationDrawerState = if (_state.value.navigationDrawerState.isOpened()) AppNavigationDrawerState.CLOSED
                    else AppNavigationDrawerState.OPENED
                )
            }

            is MainUiAction.NavigateToDrawerScreen -> {
                if (action.screen != AppDrawerScreen.THEME) _state.value = _state.value.copy(
                    navigationDrawerState = AppNavigationDrawerState.CLOSED
                )

                viewModelScope.launch {
                    _uiEvent.send(MainUiEvent.Navigate(action.screen.toDtoCoreScreen()))
                }
            }

            MainUiAction.ToggleNavigationRail -> {
                _state.value = _state.value.copy(
                    navigationRailState = if (_state.value.navigationRailState.isOpened()) AppNavigationRailState.CLOSED
                    else AppNavigationRailState.OPENED
                )
            }

            is MainUiAction.NavigateToNavigationRailScreen -> {
                if (_state.value.navigationRailScreen == action.screen) return

                if (action.screen != AppNavigationRailScreen.THEME) _state.update {
                    it.copy(
                        navigationRailScreen = action.screen,
                        navigationBottomBarScreen = action.screen.toAppBottomBarScreen()
                            ?: it.navigationBottomBarScreen,
                        navigationRailState = AppNavigationRailState.CLOSED
                    )
                }

                viewModelScope.launch {
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
            _state.value = _state.value.copy(user = user.toUiUser())
        }
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