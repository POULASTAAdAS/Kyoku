package com.poulastaa.play.presentation.view

import com.poulastaa.play.presentation.view.components.ViewDataType

sealed interface ViewOtherScreen {
    data class ViewSongArtists(val id: Long) : ViewOtherScreen
    data class AddSongToPlaylist(val id: Long) : ViewOtherScreen
    data class CreatePlaylistScreen(val playlistId: Long) : ViewOtherScreen

    sealed interface PlayOperation : ViewOtherScreen {
        data class PlayAll(val id: Long, val type: ViewDataType) : PlayOperation
        data class Shuffle(val id: Long, val type: ViewDataType) : PlayOperation
        data class PlayOne(
            val songId: Long,
            val otherId: Long,
            val type: ViewDataType,
        ) : PlayOperation
    }
}