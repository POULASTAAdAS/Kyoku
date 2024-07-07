package com.poulastaa.play.presentation.root_drawer

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.core.presentation.designsystem.components.AppScreenWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.play.presentation.root_drawer.components.compact.RootDrawerCompact
import com.poulastaa.play.presentation.root_drawer.components.expanded.RootDrawerExpanded
import kotlinx.coroutines.launch

@Composable
fun RootDrawerScreen(
    viewModel: RootDrawerViewModel = hiltViewModel(),
    navigate: (ScreenEnum) -> Unit,
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()

    ObserveAsEvent(flow = viewModel.uiEvent) { event ->
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

    AppDrawer(
        drawerState = drawerState,
        navController = navController,
        state = viewModel.state,
        onEvent = {
            if (it == RootDrawerUiEvent.OnDrawerToggle) {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open()
                    else drawerState.close()
                }
            } else {
                viewModel.onEvent(it)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun AppDrawer(
    drawerState: DrawerState,
    navController: NavHostController,
    state: RootDrawerUiState,
    onEvent: (RootDrawerUiEvent) -> Unit,
) {
    val context = LocalContext.current
    val windowSize = calculateWindowSizeClass(activity = context as Activity)

    AppScreenWindowSize(
        windowSizeClass = windowSize,
        compactContent = {
            RootDrawerCompact(
                drawerState = drawerState,
                navController = navController,
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            RootDrawerCompact(
                drawerState = drawerState,
                navController = navController,
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            RootDrawerExpanded(
                navController = navController,
                state = state,
                onSaveScreenToggle = {
                    onEvent(RootDrawerUiEvent.SaveScreenToggle(it))
                },
                onEvent = onEvent,
            )
        }
    )
}