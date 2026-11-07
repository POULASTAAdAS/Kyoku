package com.poulastaa.common.viewmodels.root

import androidx.lifecycle.viewModelScope
import com.poulastaa.common.domain.model.DtoSetupScreen
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

    private fun DtoSetupScreen?.toStartGraph(): Screens = when (this) {
        null -> Screens.AuthGraph
        DtoSetupScreen.HOME -> Screens.MainGraph
        DtoSetupScreen.IMPORT_PLAYLIST,
        DtoSetupScreen.SELECT_GENRE,
        DtoSetupScreen.SELECT_ARTIST,
            -> Screens.SetupGraph
    }

    private fun DtoSetupScreen?.toStartDestination(): Screens.SetupScreens = when (this) {
        DtoSetupScreen.SELECT_GENRE -> Screens.SetupScreens.SelectGenre
        DtoSetupScreen.SELECT_ARTIST -> Screens.SetupScreens.SelectArtist
        null,
        DtoSetupScreen.IMPORT_PLAYLIST,
        DtoSetupScreen.HOME,
            -> Screens.SetupScreens.ImportPlaylist
    }
}
