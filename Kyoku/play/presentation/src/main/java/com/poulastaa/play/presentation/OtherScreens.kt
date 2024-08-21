package com.poulastaa.play.presentation

import com.poulastaa.play.presentation.view.components.ViewDataType


sealed interface OtherScreens {
    data class AddAsPlaylist(
        val songId: Long
    ) : OtherScreens

    data class View(
        val id: Long = -1,
        val type: ViewDataType
    ) : OtherScreens
}