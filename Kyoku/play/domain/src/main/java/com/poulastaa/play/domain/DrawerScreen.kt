package com.poulastaa.play.domain

sealed class DrawerScreen(val route: String) {
    data object Home : DrawerScreen(route = "/home")
    data object Library : DrawerScreen(route = "/library")

    data object AddToPlaylist : DrawerScreen(route = "/addToPlaylist") {
        const val ROUTE_EXT = "/{songId}"
        const val SONG_ID = "songId"
    }

    data object Profile : DrawerScreen(route = "/profile")
    data object History : DrawerScreen(route = "/history")
    data object Settings : DrawerScreen(route = "/settings")
}