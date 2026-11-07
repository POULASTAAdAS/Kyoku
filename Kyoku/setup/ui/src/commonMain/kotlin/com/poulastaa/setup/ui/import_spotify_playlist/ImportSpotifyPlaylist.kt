package com.poulastaa.setup.ui.import_spotify_playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalFocusManager
import com.poulastaa.common.domain.model.DtoSetupScreen
import com.poulastaa.common.domain.preferences.AppPreferences
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import com.poulastaa.common.viewmodels.Screens
import com.poulastaa.setup.ui.import_spotify_playlist.screens.CompatVerticalImportSpotifyPlaylistScreen
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistViewmodel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ImportSpotifyPlaylist(
    viewmodel: ImportPlaylistViewmodel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val state by viewmodel.uiState.collectAsState()
    val navController = LocalNavController.current
    val appPreferences = koinInject<AppPreferences>()
    val scope = rememberCoroutineScope()

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalImportSpotifyPlaylistScreen(
                focusManager,
                state,
                viewmodel,
                onContinueClick = {
                    scope.launch {
                        appPreferences.saveLastSetupScreen(DtoSetupScreen.SELECT_GENRE)
                        navController.navigate(Screens.SetupScreens.SelectGenre)
                    }
                },
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }
}
