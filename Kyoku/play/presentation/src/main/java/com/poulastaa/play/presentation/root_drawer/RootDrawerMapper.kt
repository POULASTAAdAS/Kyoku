package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen

fun String.toDrawerScreen() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home
    else -> DrawerScreen.Library
}

fun String.toSaveScreen() = when (this) {
    SaveScreen.HOME.name -> SaveScreen.HOME
    else -> SaveScreen.LIBRARY
}


fun String.toDrawScreenRoute() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home.route
    else -> DrawerScreen.Library.route
}

fun PrevSavedPlaylist.toUiPlaylist() = UiPrevPlaylist(
    id = this.id,
    name = this.name,
    urls = this.coverImageList
)
