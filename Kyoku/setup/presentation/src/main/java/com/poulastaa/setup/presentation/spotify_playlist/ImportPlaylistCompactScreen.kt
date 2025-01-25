package com.poulastaa.setup.presentation.spotify_playlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
internal fun ImportPlaylistCompactScreen(
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    ImportPlaylistScreenWrapper(
        modifier = Modifier.fillMaxWidth(.7f),
        state = state,
        floatingActionButtonText = state.floatActionButtonText,
        contentPadding = MaterialTheme.dimens.small3,
        contentSpacePadding = MaterialTheme.dimens.small2,
        onFloatingActionButtonClick = {
            onAction(ImportPlaylistUiAction.OnSkipClick)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            focusManager.clearFocus()
        },
        onLinkChange = {
            onAction(ImportPlaylistUiAction.OnLinkChange(it))
        },
        onImportClick = {
            onAction(ImportPlaylistUiAction.OnImportClick)
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            focusManager.clearFocus()
        },
        onPlaylistClick = { id ->
            onAction(ImportPlaylistUiAction.OnPlaylistClick(id))
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    )
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ImportPlaylistCompactScreen(
                state = ImportPlaylistUiState(),
                onAction = {}
            )
        }
    }
}