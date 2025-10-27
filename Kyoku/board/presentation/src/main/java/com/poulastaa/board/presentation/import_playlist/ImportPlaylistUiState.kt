package com.poulastaa.board.presentation.import_playlist

import androidx.compose.runtime.Stable
import com.poulastaa.core.presentation.designsystem.TextProp
import com.poulastaa.core.presentation.designsystem.UiPrevPlaylistSong
import kotlin.random.Random

@Stable
internal data class ImportPlaylistUiState(
    val isLoading: Boolean = false,
    val isMakingApiCall: Boolean = false,
    val isSkipping: Boolean = false,
    val link: TextProp = TextProp(),

    val data: List<UiPreviewPlaylist> = (1..5).map {playlistId ->
        UiPreviewPlaylist(
            id = playlistId.toLong(),
            title = "Playlist $playlistId",
            songs = (1..5).map { songId ->
                UiPrevPlaylistSong(
                    id = songId.toLong(),
                    title = "Song $songId",
                    artist = "Artist $songId",
                )
            },
            isExpanded = Random.nextBoolean()
        )
    },
)