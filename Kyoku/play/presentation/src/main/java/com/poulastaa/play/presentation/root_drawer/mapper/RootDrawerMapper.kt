package com.poulastaa.play.presentation.root_drawer.mapper

import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen

fun String.toSavedScreen() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home.route
    else -> DrawerScreen.Library.route
}

fun String.toDrawScreenRoute() = when(this){
    SaveScreen.HOME.name -> DrawerScreen.Home.route
    else -> DrawerScreen.Library.route
}