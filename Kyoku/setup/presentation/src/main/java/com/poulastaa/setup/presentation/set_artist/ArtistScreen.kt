package com.poulastaa.setup.presentation.set_artist

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.AppTopAppbar
import com.poulastaa.core.presentation.designsystem.components.SetupScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.setup.presentation.components.ContinueFloatingActionButton
import com.poulastaa.setup.presentation.set_artist.components.ArtistCompact

@Composable
fun ArtistRootScreen(
    viewModel: ArtistViewModel = hiltViewModel(),
    navigateToHome: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is ArtistUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    it.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            ArtistUiAction.NavigateToHome -> navigateToHome()
        }
    }

    ArtistScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun ArtistScreen(
    state: ArtistUiState,
    onEvent: (ArtistUiEvent) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity = context)


    SetupScreenWrapper(
        windowSizeClass = windowSize,
        topBar = {
            AppTopAppbar(
                isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded,
                text = stringResource(id = R.string.set_artist_title)
            )
        },
        floatingActionButton = {
            AnimatedVisibility(visible = state.canMakeApiCall) {
                val temp = remember {
                    state.canMakeApiCall
                }

                if (temp) ContinueFloatingActionButton {
                    onEvent(ArtistUiEvent.OnContinueClick)
                }
            }
        },
        compactContent = {
            ArtistCompact(
                paddingValues = it,
                state = state,
                onEvent = onEvent
            )
        },
        mediumContent = {
            ArtistCompact(
                paddingValues = it,
                grid = 4,
                state = state,
                onEvent = onEvent
            )
        },
        expandedContent = {
            ArtistCompact(
                paddingValues = it,
                grid = 4,
                state = state,
                onEvent = onEvent
            )
        }
    )
}