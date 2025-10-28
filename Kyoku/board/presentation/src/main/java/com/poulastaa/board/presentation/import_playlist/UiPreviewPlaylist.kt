package com.poulastaa.board.presentation.import_playlist

import androidx.compose.runtime.Stable
import com.poulastaa.core.domain.utils.InternalId
import com.poulastaa.core.presentation.designsystem.UiPrevSong

@Stable
data class UiPreviewPlaylist(
    val internalId: InternalId,
    val id: Long = -1,
    val title: String = "",
    val songs: List<UiPrevSong> = emptyList(),
    val isExpanded: Boolean = false,
)