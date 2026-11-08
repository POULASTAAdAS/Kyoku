package com.poulastaa.setup.viewmodels.import_playlist

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.poulastaa.common.viewmodels.states.UiTextFiledState

data class ImportPlaylistUiState(
    val isLoading: Boolean = true,
    val isMakingApiCall: Boolean = false,
    val link: UiTextFiledState = UiTextFiledState(),
    val playlists: List<UiPlaylist> = emptyList(),
)

@Stable
@Immutable
data class UiDuration(
    val time: String,
    val unit: UiTimeUnit,
) {
    enum class UiTimeUnit {
        HOURS,
        MINUTES,
    }
}

@Stable
@Immutable
data class UiPlaylist(
    val id: Long,
    val title: String,
    val totalDuration: UiDuration? = null,
    val posters: List<String>,
    val tracks: List<UiImportPlaylistTrack>,
    val isExpanded: Boolean = false,
)

@Stable
@Immutable
data class UiImportPlaylistTrack(
    val id: Long,
    val title: String,
    val duration: UiDuration? = null,
    val artist: List<String>,
    val poster: String? = null,
)
