package com.poulastaa.play.domain

sealed class DrawerScreen(val route: String) {
    data object Home : DrawerScreen(route = "/home")
    data object Library : DrawerScreen(route = "/library")

    data object AddToPlaylist : DrawerScreen(route = "/addToPlaylist")

    data object Profile : DrawerScreen(route = "/profile")
    data object History : DrawerScreen(route = "/history")
    data object Settings : DrawerScreen(route = "/settings")
}