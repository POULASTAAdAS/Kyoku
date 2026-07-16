package com.poulastaa.setup.ui.import_spotify_playlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalFocusManager
import com.poulastaa.common.ui.LocalNavController
import com.poulastaa.common.ui.components.ScreenSizeType
import com.poulastaa.common.ui.components.ScreenSizeWrapper
import com.poulastaa.setup.ui.import_spotify_playlist.screens.CompatVerticalImportSpotifyPlaylistScreen
import kotlin.random.Random

@Stable
@Immutable
data class UiDuration(
    val time: String,
    val unit: UiTimeUnit,
) {
    enum class UiTimeUnit(val value: String) {
        HOURS("hours"),
        MINUTES("minutes"),
    }
}

@Stable
@Immutable
data class UiPlaylist(
    val id: Long,
    val title: String,
    val totalDuration: UiDuration,
    val posters: List<String>,
    val tracks: List<UiImportPlaylistTrack>,
    val isExpanded: Boolean = Random.nextBoolean(),
)

@Stable
@Immutable
data class UiImportPlaylistTrack(
    val id: Long,
    val title: String,
    val duration: UiDuration,
    val artist: List<String>,
    val poster: String? = null,
)

var dummyData by mutableStateOf((1..3).map {
    UiPlaylist(
        id = it.toLong(),
        title = "Playlist $it",
        totalDuration = UiDuration(
            time = Random.nextInt(5, 15).toString(),
            unit = UiDuration.UiTimeUnit.HOURS
        ),
        posters = (0..Random.nextInt(0, 5)).map { "https://picsum.photos/200" },
        tracks = (1..Random.nextInt(5, 10)).map {
            UiImportPlaylistTrack(
                id = it.toLong(),
                title = "Track $it",
                duration = UiDuration(
                    time = "5:00",
                    unit = UiDuration.UiTimeUnit.MINUTES
                ),
                artist = listOf("That Cool Artist"),
                poster = null
            )
        }
    )
})

@Composable
fun ImportSpotifyPlaylist() {
    val focusManager = LocalFocusManager.current
    var isLoading by mutableStateOf(false)
    var link by remember { mutableStateOf("") }
    val navController = LocalNavController.current

    ScreenSizeWrapper { screenSizeType ->
        when (screenSizeType) {
            ScreenSizeType.CompactVertical -> CompatVerticalImportSpotifyPlaylistScreen(
                focusManager,
                isLoading,
                onLoadingChange = { isLoading = it },
                link,
                onLinkChange = { link = it },
                navController,
                dummyData,
                onPlaylistsChange = { dummyData = it },
            )

            ScreenSizeType.CompactHorizontal -> TODO()

            ScreenSizeType.LargeVertical -> TODO()
            ScreenSizeType.LargeHorizontal -> TODO()
        }
    }
}
