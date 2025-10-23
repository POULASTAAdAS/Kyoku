package com.poulastaa.board.presentation.import_playlist

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.poulastaa.core.presentation.StateFullKyokuWindowSize
import com.poulastaa.core.presentation.designsystem.ObserveAsEvent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ImportPlaylistRootScreen(
    navigateToSelectBDate: () -> Unit,
) {
    val viewmodel = hiltViewModel<ImportPlaylistViewmodel>()
    val state by viewmodel.state.collectAsStateWithLifecycle()

    val activity = LocalActivity.current ?: return

    ObserveAsEvent(viewmodel.uiEvent) { event ->
        when (event) {
            is ImportPlaylistUiEvent.EmitToast -> Toast.makeText(
                activity, event.message.asString(activity),
                Toast.LENGTH_LONG
            ).show()

            ImportPlaylistUiEvent.NavigateToSelectBDate -> navigateToSelectBDate()
        }
    }

    StateFullKyokuWindowSize(
        sharedFlow = viewmodel.eventManager.rootEvent,
        windowSizeClass = calculateWindowSizeClass(activity),
        compactContent = {

        },
        mediumContent = {

        },
        expandedSmallContent = {

        },
        expandedCompactContent = {

        },
        expandedLargeContent = {

        }
    )
}