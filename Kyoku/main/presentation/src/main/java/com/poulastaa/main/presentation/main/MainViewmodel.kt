package com.poulastaa.main.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.repository.DatastoreRepository
import com.poulastaa.core.presentation.designsystem.toUiUser
import com.poulastaa.main.domain.model.AppDrawerScreen
import com.poulastaa.main.domain.model.AppDrawerState
import com.poulastaa.main.domain.model.AppNavigationRailState
import com.poulastaa.main.domain.model.isOpened
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
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

    fun onAction(action: MainUiAction) {
        when (action) {
            MainUiAction.ToggleDrawer -> {
                _state.value = _state.value.copy(
                    drawerState = if (_state.value.drawerState.isOpened()) AppDrawerState.CLOSED
                    else AppDrawerState.OPENED
                )
            }

            is MainUiAction.NavigateToDrawerScreen -> {
                if (action.screen != AppDrawerScreen.THEME) {
                    _state.value = _state.value.copy(
                        drawerState = AppDrawerState.CLOSED
                    )

                    // todo navigate to screen
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
                _state.update {
                    it.copy(
                        navigationRailScreen = action.screen
                    )
                }

                // todo navigate to screen
            }

            is MainUiAction.NavigateBottomBarScreen -> {
                if (_state.value.navigationBottomBarScreen == action.screen) return
                _state.update {
                    it.copy(
                        navigationBottomBarScreen = action.screen
                    )
                }

                // todo navigate to screen
            }
        }
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = ds.readLocalUser()
            _state.value = _state.value.copy(user = user.toUiUser())
        }
    }
}