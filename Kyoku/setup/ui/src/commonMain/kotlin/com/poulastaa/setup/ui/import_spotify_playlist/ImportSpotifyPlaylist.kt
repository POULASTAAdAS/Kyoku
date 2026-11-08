package com.poulastaa.setup.ui.import_spotify_playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import com.poulastaa.common.viewmodels.Screens
import com.poulastaa.setup.ui.import_spotify_playlist.screens.CompatVerticalImportSpotifyPlaylistScreen
import com.poulastaa.setup.viewmodels.import_playlist.ImportPlaylistViewmodel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ImportSpotifyPlaylist(
    viewmodel: ImportPlaylistViewmodel = koinViewModel(),
) {
    val focusManager = LocalFocusManager.current
    val state by viewmodel.uiState.collectAsState()
    val navController = LocalNavController.current

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalImportSpotifyPlaylistScreen(
                focusManager,
                state,
                viewmodel,
                onContinueClick = {
                    navController.navigate(Screens.SetupScreens.SelectGenre)
                },
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }
}
