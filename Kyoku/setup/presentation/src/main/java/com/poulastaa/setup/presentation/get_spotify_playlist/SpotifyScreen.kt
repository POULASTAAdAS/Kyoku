package com.poulastaa.setup.presentation.get_spotify_playlist

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.setup.presentation.get_spotify_playlist.components.SpotifyCompactScreen
import com.poulastaa.setup.presentation.get_spotify_playlist.components.SpotifyExtendedScreen
import com.poulastaa.setup.presentation.get_spotify_playlist.components.SpotifyFloatingActionButton
import com.poulastaa.setup.presentation.get_spotify_playlist.components.SpotifyMediumScreen
import com.poulastaa.setup.presentation.get_spotify_playlist.components.SpotifyTopAppbar

@Composable
fun SpotifyRootScreen(
    viewModel: SpotifyViewModel = hiltViewModel(),
    navigateToSetBDate: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is SpotifyUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    it.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            SpotifyUiAction.NavigateToSetBDate -> navigateToSetBDate()
        }
    }

    SpotifyScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun SpotifyScreen(
    state: SpotifyPlaylistUiState,
    onEvent: (SpotifyPlaylistUiEvent) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity = context)

    Scaffold(
        topBar = {
            SpotifyTopAppbar(
                isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = !state.isMakingApiCall) {
                val temp = remember {
                    !state.isMakingApiCall
                }

                if (temp) SpotifyFloatingActionButton(
                    isData = state.playlists.isNotEmpty(),
                    isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded,
                    onClick = {
                        onEvent(SpotifyPlaylistUiEvent.OnSkipClick)
                    }
                )
            }
        }
    ) {
        when (windowSize.widthSizeClass) {
            WindowWidthSizeClass.Compact -> {
                SpotifyCompactScreen(
                    state = state, paddingValues = it,
                    onEvent = onEvent
                )
            }

            WindowWidthSizeClass.Medium -> {
                SpotifyMediumScreen(
                    state = state, paddingValues = it,
                    onEvent = onEvent
                )
            }

            WindowWidthSizeClass.Expanded -> {
                SpotifyExtendedScreen(
                    state = state, paddingValues = it,
                    onEvent = onEvent
                )
            }
        }
    }
}