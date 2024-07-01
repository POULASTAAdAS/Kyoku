package com.poulastaa.setup.presentation.set_b_date

import android.app.Activity
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.poulastaa.core.presentation.designsystem.R
import com.poulastaa.core.presentation.designsystem.components.LockScreenOrientation
import com.poulastaa.core.presentation.designsystem.components.SetupScreenWrapper
import com.poulastaa.core.presentation.ui.ObserveAsEvent
import com.poulastaa.setup.presentation.set_b_date.components.BDateCompact
import com.poulastaa.setup.presentation.set_b_date.components.BDateMedium
import com.poulastaa.setup.presentation.set_b_date.components.BDatePicker
import com.poulastaa.setup.presentation.set_b_date.components.BDateTopBar

@Composable
fun BDateRootScreen(
    viewModel: BDateViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToPicGenre: () -> Unit,
) {
    val context = LocalContext.current

    ObserveAsEvent(flow = viewModel.uiEvent) {
        when (it) {
            is BDateUiAction.EmitToast -> {
                Toast.makeText(
                    context,
                    it.message.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            BDateUiAction.NavigateToSpotifyPlaylist -> navigateBack()
            BDateUiAction.NavigateToSetGenre -> navigateToPicGenre()
        }
    }

    BDateScreen(
        state = viewModel.state,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun BDateScreen(
    state: BDateUiState,
    onEvent: (BDateUiEvent) -> Unit,
) {
    val context = LocalContext.current as Activity
    val windowSize = calculateWindowSizeClass(activity = context)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SetupScreenWrapper(
            windowSizeClass = windowSize,
            topBar = {
                BDateTopBar(
                    isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded,
                    text = stringResource(id = R.string.set_bDate_top_appbar_title),
                    navigateBack = {
                        onEvent(BDateUiEvent.OnBackClick)
                    }
                )
            },
            compactContent = {
                LockScreenOrientation(orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

                BDateCompact(
                    paddingValues = it,
                    state = state,
                    onEvent = onEvent
                )
            },
            mediumContent = {
                BDateMedium(
                    paddingValues = it,
                    width = .7f,
                    state = state,
                    onEvent = onEvent
                )
            },
            expandedContent = {
                BDateMedium(
                    paddingValues = it,
                    width = .6f,
                    state = state,
                    onEvent = onEvent
                )
            }
        )

        AnimatedVisibility(visible = state.isDateDialogOpen && !state.isMakingApiCall) {
            BDatePicker(
                isExpanded = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded,
                onDateSelected = {
                    it?.let { onEvent(BDateUiEvent.OnBDateSubmit(it)) }
                    onEvent(BDateUiEvent.OnBDateDialogToggle)
                },
                dismissReq = {
                    onEvent(BDateUiEvent.OnBDateDialogToggle)
                }
            )
        }

        BackHandler {
            if (state.isDateDialogOpen) onEvent(BDateUiEvent.OnBDateDialogToggle)
            else onEvent(BDateUiEvent.OnBackClick)
        }
    }
}