package com.poulastaa.play.presentation.root_drawer.mapper

import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen

fun String.toDrawerScreen() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home
    else -> DrawerScreen.Library
}

fun String.toSaveScreen() = when(this){
    SaveScreen.HOME.name -> SaveScreen.HOME
    else -> SaveScreen.LIBRARY
}


fun String.toDrawScreenRoute() = when (this) {
    SaveScreen.HOME.name -> DrawerScreen.Home.route
    else -> DrawerScreen.Library.route
}