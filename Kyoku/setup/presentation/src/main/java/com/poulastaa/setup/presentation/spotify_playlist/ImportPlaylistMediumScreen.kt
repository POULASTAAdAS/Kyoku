package com.poulastaa.setup.presentation.spotify_playlist

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import com.poulastaa.core.presentation.designsystem.AppThem
import com.poulastaa.core.presentation.designsystem.dimens

@Composable
internal fun ImportPlaylistMediumScreen(
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val haptic = LocalHapticFeedback.current

    ImportPlaylistScreenWrapper(
        modifier = Modifier.fillMaxWidth(.8f),
        state = state,
        floatingActionButtonText = state.floatActionButtonText,
        floatingActionButtonWidth = .45f,
        contentPadding = MaterialTheme.dimens.small2,
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


@Preview(
    device = "spec:width=700dp,height=1280dp,dpi=480",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Preview(
    device = "spec:width=700dp,height=1280dp,dpi=480",
)
@Composable
private fun Preview() {
    AppThem {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            ImportPlaylistMediumScreen(
                state = ImportPlaylistUiState(),
                onAction = {}
            )
        }
    }
}