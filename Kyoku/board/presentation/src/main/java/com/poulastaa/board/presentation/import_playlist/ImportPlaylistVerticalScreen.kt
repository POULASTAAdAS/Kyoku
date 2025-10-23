package com.poulastaa.board.presentation.import_playlist

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.poulastaa.core.presentation.ui.AppTheme
import com.poulastaa.core.presentation.ui.PreviewCompactPortrait

@Composable
internal fun ImportPlaylistVerticalScreen(
    modifier: Modifier = Modifier,
    state: ImportPlaylistUiState,
    onAction: (ImportPlaylistUiAction) -> Unit,
) {

}

@PreviewCompactPortrait
@Composable
private fun Preview() {
    AppTheme(isSystemInDarkTheme()) {
        ImportPlaylistVerticalScreen(
            state = ImportPlaylistUiState(

            )
        ) { }
    }
}