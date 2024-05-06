package com.poulastaa.kyoku.data.model.home_nav_drawer

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.poulastaa.kyoku.data.model.screens.player.DragAnchors
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong
import com.poulastaa.kyoku.data.model.screens.view_aritst.ViewArtistUiArtist

@Stable
@OptIn(ExperimentalFoundationApi::class)
data class HomeRootUiState(
    val isFirst: Boolean = true,

    val isCookie: Boolean = false,
    val headerValue: String = "",

    val isLoading: Boolean = false,

    val homeTopBarTitle: String = "Good Morning",
    val libraryTopBarTitle: String = "Your Library",
    val profilePicUrl: String = "",
    val userName: String = "",
    val nav: Nav = Nav.HOME,

    val anchors: AnchoredDraggableState<DragAnchors> = AnchoredDraggableState(
        initialValue = DragAnchors.START,
        positionalThreshold = { it * 0.5f },
        velocityThreshold = { 1f },
        animationSpec = tween()
    ).apply {
        updateAnchors(
            DraggableAnchors {
                DragAnchors.START at 0f
                DragAnchors.END at 250f
            }
        )
    },

    val player: Player = Player()
)

@Stable
data class Player(
    val isSmallPlayer: Boolean = false,
    val isPlayerOpen: Boolean = false,
    val isLoading: Boolean = true,

    val allSong: List<QueueSong> = emptyList(),
    val info: PlayingSongInfo = PlayingSongInfo(),

    val playingSongData: PlayingSongData = PlayingSongData(),

    val isShuffle: Boolean = false,
    val isRepeat: Boolean = false,

    val isPlaying: Boolean = false,
    val hasNext: Boolean = false,
    val hasPrev: Boolean = false,

    val progress: Float = 0f,
    val playingIndex: Int = -1,

    val colors: List<Color> = emptyList()
)

@Stable
data class QueueSong(
    val isPlaying: Boolean = false,
    val playerSong: PlayerSong
)

@Stable
data class PlayingSongInfo(
    val id: Long = -1,
    val typeName: String = "Kyoku"
)

@Stable
data class PlayingSongData(
    val isAdditionalDataLoaded: Boolean = false,
    val releaseDate: String = "",
    val playingSong: PlayerSong = PlayerSong(),
    val playingSongArtist: List<ViewArtistUiArtist> = emptyList(),
    val playingSongAlbum: PlayingSongAlbum = PlayingSongAlbum()
)

@Stable
data class PlayingSongAlbum(
    val albumId: Long = -1,
    val name: String = ""
)

@Stable
enum class Nav {
    HOME,
    LIB,
    NON
}
