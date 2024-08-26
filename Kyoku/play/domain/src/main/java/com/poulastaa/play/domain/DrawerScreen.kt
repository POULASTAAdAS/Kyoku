package com.poulastaa.play.domain

sealed class DrawerScreen(val route: String) {
    data object Home : DrawerScreen(route = "/home")
    data object Library : DrawerScreen(route = "/library")

    data object ViewArtist: DrawerScreen(route = "/viewArtist"){
        const val PARAM = "/{id}"
    }

    data object Profile : DrawerScreen(route = "/profile")
    data object History : DrawerScreen(route = "/history")
    data object Settings : DrawerScreen(route = "/settings")
}