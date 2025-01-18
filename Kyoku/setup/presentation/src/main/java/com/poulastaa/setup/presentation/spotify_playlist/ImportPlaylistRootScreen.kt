package com.poulastaa.setup.presentation.spotify_playlist

import android.app.Activity
import android.widget.Toast
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.designsystem.gradiantBackground
import com.poulastaa.core.presentation.ui.KyokuWindowSize
import com.poulastaa.core.presentation.ui.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ImportPlaylistRootScreen(
    viewmodel: ImportPlaylistViewmodel = hiltViewModel(),
    navigateToBDate: () -> Unit,
) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)

    val state by viewmodel.state.collectAsStateWithLifecycle()

    val haptic = LocalHapticFeedback.current

    ObserveAsEvent(viewmodel.uiState) { event ->
        when (event) {
            is ImportPlaylistUiEvent.EmitToast -> Toast.makeText(
                activity,
                event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            ImportPlaylistUiEvent.OnSuccess -> haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            ImportPlaylistUiEvent.NavigateToBDate -> navigateToBDate()
        }
    }

    KyokuWindowSize(
        windowSizeClass = windowSizeClass,
        compactContent = {
            ImportPlaylistCompactScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        },
        mediumContent = {
            ImportPlaylistMediumScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        },
        expandedContent = {
            ImportPlaylistExpandedScreen(
                state = state,
                onAction = viewmodel::onAction
            )
        }
    )
}