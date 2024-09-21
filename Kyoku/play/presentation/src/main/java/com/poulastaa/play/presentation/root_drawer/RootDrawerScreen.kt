package com.poulastaa.play.presentation.root_drawer

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.components.AppScreenWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.player.PlayerUiEvent
import com.poulastaa.play.presentation.player.PlayerUiState
import com.poulastaa.play.presentation.root_drawer.components.compact.RootDrawerCompact
import com.poulastaa.play.presentation.root_drawer.components.expanded.RootDrawerExpandedLarge
import com.poulastaa.play.presentation.root_drawer.components.expanded.RootDrawerExpandedSmall
import kotlinx.coroutines.launch

@Composable
fun RootDrawerScreen(
    drawerViewModel: RootDrawerViewModel = hiltViewModel(),
    playerViewModel: PlayerViewModel = hiltViewModel(),
    navigate: (ScreenEnum) -> Unit,
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ObserveAsEvent(flow = drawerViewModel.uiEvent) { event ->
        when (event) {
            is RootDrawerUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    event.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is RootDrawerUiAction.Navigate -> navigate(event.screen)
        }
    }

    ObserveAsEvent(flow = playerViewModel.uiEvent) { event ->
        Toast.makeText(
            context,
            event.message.asString(context),
            Toast.LENGTH_LONG
        ).show()
    }

    AppDrawer(
        drawerState = drawerState,
        drawerUiState = drawerViewModel.state,
        playerUiState = playerViewModel.state,
        onEvent = {
            if (it == RootDrawerUiEvent.OnDrawerToggle) {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open()
                    else drawerState.close()
                }
            } else {
                drawerViewModel.onEvent(it)
            }
        },
        onPlayerEvent = playerViewModel::onPlayerEvent
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun AppDrawer(
    drawerState: DrawerState,
    drawerUiState: RootDrawerUiState,
    playerUiState: PlayerUiState,
    onEvent: (RootDrawerUiEvent) -> Unit,
    onPlayerEvent: (PlayerUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val windowSize = calculateWindowSizeClass(activity = context as Activity)
    val navController = rememberNavController()
    val config = LocalConfiguration.current
    val viewSongArtistSheetState = rememberModalBottomSheetState()

    AppScreenWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            RootDrawerCompact(
                viewSongArtistSheetState = viewSongArtistSheetState,
                isSmall = true,
                drawerState = drawerState,
                navController = navController,
                drawerUiState = drawerUiState,
                playerUiState = playerUiState,
                onSaveScreenToggle = {
                    onEvent(RootDrawerUiEvent.SaveScreenToggle(it))
                },
                onEvent = onEvent,
                onPlayerEvent = onPlayerEvent
            )
        },
        mediumContent = {
            RootDrawerCompact(
                viewSongArtistSheetState = viewSongArtistSheetState,
                isSmall = false,
                drawerState = drawerState,
                navController = navController,
                drawerUiState = drawerUiState,
                playerUiState = playerUiState,
                onSaveScreenToggle = {
                    onEvent(RootDrawerUiEvent.SaveScreenToggle(it))
                },
                onEvent = onEvent,
                onPlayerEvent = onPlayerEvent
            )
        },
        expandedContent = {
            if (config.screenWidthDp > 980) RootDrawerExpandedLarge(
                viewSongArtistSheetState = viewSongArtistSheetState,
                navController = navController,
                drawerUiState = drawerUiState,
                playerUiState = playerUiState,
                onEvent = onEvent,
                onPlayerEvent = onPlayerEvent
            ) else RootDrawerExpandedSmall(
                viewSongArtistSheetState = viewSongArtistSheetState,
                navController = navController,
                drawerUiState = drawerUiState,
                playerUiState = playerUiState,
                onEvent = onEvent,
                onPlayerEvent = onPlayerEvent
            )
        }
    )
}