package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.presentation.root_drawer.home.HomeAddToPlaylistUiState

data class RootDrawerUiState(
    val saveScreen: SaveScreen = SaveScreen.HOME,
    val startDestination: String = DrawerScreen.Home.route,
    val isScreenLoaded: Boolean = false,

    val username: String = "User",
    val profilePicUrl: String = "",

    val addToPlaylistUiState: HomeAddToPlaylistUiState = HomeAddToPlaylistUiState()
)
