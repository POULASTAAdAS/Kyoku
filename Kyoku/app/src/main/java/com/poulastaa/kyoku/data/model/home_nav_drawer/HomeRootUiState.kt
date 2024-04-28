package com.poulastaa.kyoku.data.model.home_nav_drawer

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.poulastaa.kyoku.data.model.screens.player.DragAnchors
import com.poulastaa.kyoku.data.model.screens.player.PlayerSong

@Stable
data class HomeRootUiState @OptIn(ExperimentalFoundationApi::class) constructor(
    val isCookie: Boolean = false,
    val isLoading: Boolean = false,
    val headerValue: String = "",
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

    val isRepeat: Boolean = false,

    val allSong: List<PlayerSong> = emptyList(),
    val info: PlayingSongInfo = PlayingSongInfo(),
    val playingSong: PlayerSong = PlayerSong(),

    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val playingIndex: Int = -1,

    val colors: List<Color> = emptyList()
)

data class PlayingSongInfo(
    val id: Long = -1,
    val typeName: String = "Kyoku"
)

@Stable
enum class Nav {
    HOME,
    LIB,
    NON
}
