package com.poulastaa.board.presentation.import_playlist

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.TextProp

@Stable
internal data class ImportPlaylistUiState(
    val isLoading: Boolean = false,
    val isMakingApiCall: Boolean = false,
    val isSkipping: Boolean = false,
    val link: TextProp = TextProp(),

    val data: List<UiPreviewPlaylist> = emptyList(),
)