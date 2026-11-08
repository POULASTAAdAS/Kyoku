package com.poulastaa.common.viewmodels.root

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.domain.model.DtoSavedScreen
import com.poulastaa.common.domain.preferences.AppPreferences
import com.poulastaa.common.viewmodels.BaseViewmodel
import com.poulastaa.common.viewmodels.Screens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RootViewmodel(
    private val appPreferences: AppPreferences,
) : BaseViewmodel<RootUiState, Nothing, Nothing>(
    initialSate = RootUiState(),
) {
    init {
        viewModelScope.launch {
            delay(1600.milliseconds) // keep splash on-screen until the animated icon finishes (~1580ms), matching windowSplashScreenAnimationDuration
            val lastSetupScreen = appPreferences.getLastSetupScreen()

            updateState {
                copy(
                    isLoading = false,
                    startDestination = lastSetupScreen.toStartGraph(),
                    setupStartDestination = lastSetupScreen.toStartDestination(),
                )
            }
        }
    }

    override suspend fun handleAction(action: Nothing) = Unit

    private fun DtoSavedScreen?.toStartGraph(): Screens = when (this) {
        null -> Screens.AuthGraph
        DtoSavedScreen.HOME -> Screens.MainGraph
        DtoSavedScreen.IMPORT_PLAYLIST,
        DtoSavedScreen.SELECT_GENRE,
        DtoSavedScreen.SELECT_ARTIST,
            -> Screens.SetupGraph
    }

    private fun DtoSavedScreen?.toStartDestination(): Screens.SetupScreens = when (this) {
        DtoSavedScreen.SELECT_GENRE -> Screens.SetupScreens.SelectGenre
        DtoSavedScreen.SELECT_ARTIST -> Screens.SetupScreens.SelectArtist
        null,
        DtoSavedScreen.IMPORT_PLAYLIST,
        DtoSavedScreen.HOME,
            -> Screens.SetupScreens.ImportPlaylist
    }
}
