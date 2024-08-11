package com.poulastaa.play.presentation.root_drawer

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.poulastaa.core.domain.DataStoreRepository
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootDrawerViewModel @Inject constructor(
    private val ds: DataStoreRepository,
) : ViewModel() {
    var state by mutableStateOf(RootDrawerUiState())
        private set

    init {
        viewModelScope.launch {
            val savedScreenStringDef = async {
                ds.readSaveScreen().first()
            }
            val userDef = async { ds.readLocalUser() }
            val savedScreen = savedScreenStringDef.await()
            val user = userDef.await()

            Log.d("user", user.toString())

            state = state.copy(
                saveScreen = savedScreen.toSaveScreen(),
                startDestination = savedScreen.toDrawerScreen().route,
                isScreenLoaded = true,
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

            else -> Unit
        }
    }


    private fun updateSaveScreen(screen: SaveScreen) = viewModelScope.launch {
        ds.storeSaveScreen(screen.name)
    }
}