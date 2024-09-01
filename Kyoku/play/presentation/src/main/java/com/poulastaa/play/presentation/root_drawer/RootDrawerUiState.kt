package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.presentation.root_drawer.home.HomeAddToPlaylistUiState
import com.poulastaa.play.presentation.view.components.ViewDataType

data class RootDrawerUiState(
    val saveScreen: SaveScreen = SaveScreen.HOME,
    val startDestination: String = DrawerScreen.Home.route,

    val username: String = "User",
    val profilePicUrl: String = "",

    val addToPlaylistUiState: HomeAddToPlaylistUiState = HomeAddToPlaylistUiState(),
    val viewUiState: HomeViewUiState = HomeViewUiState(),
    val exploreArtistUiState: ExploreArtistUiState = ExploreArtistUiState(),
    val newAlbumUiState: NewAlbumUiState = NewAlbumUiState()
)

data class HomeViewUiState(
    val isOpen: Boolean = false,
    val songId: Long = -1,
    val type: ViewDataType = ViewDataType.PLAYLIST
)

data class ExploreArtistUiState(
    val isOpen: Boolean = false,
    val artistId: Long = -1
)

data class NewAlbumUiState(
    val isOpen: Boolean = false,
)
