package com.poulastaa.play.presentation.root_drawer.library

import com.poulastaa.play.presentation.view.components.ViewDataType

sealed interface LibraryOtherScreen {
    data class View(
        val id: Long = -1,
        val type: ViewDataType
    ) : LibraryOtherScreen

    data class ViewArtist(val id: Long) : LibraryOtherScreen

    data object NewAlbum : LibraryOtherScreen
}