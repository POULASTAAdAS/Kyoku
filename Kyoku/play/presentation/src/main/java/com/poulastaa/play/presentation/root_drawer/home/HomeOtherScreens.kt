package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.play.presentation.view.components.ViewDataType


sealed interface HomeOtherScreens {
    data class AddAsPlaylist(
        val songId: Long
    ) : HomeOtherScreens

    data class View(
        val id: Long = -1,
        val type: ViewDataType
    ) : HomeOtherScreens

    data class ViewArtist(val id: Long) : HomeOtherScreens
}