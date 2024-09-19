package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.play.domain.DrawerScreen
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.presentation.player.PlayerUiState
import com.poulastaa.play.presentation.root_drawer.home.HomeAddToPlaylistUiState
import com.poulastaa.play.presentation.view.components.ViewDataType

data class RootDrawerUiState(
    val saveScreen: SaveScreen = SaveScreen.HOME,
    val startDestination: String = DrawerScreen.Home.route,

    val username: String = "User",
    val profilePicUrl: String = "",

    val header: String = "",

    val addToPlaylistUiState: HomeAddToPlaylistUiState = HomeAddToPlaylistUiState(),
    val viewUiState: HomeViewUiState = HomeViewUiState(),
    val exploreArtistUiState: ExploreArtistUiState = ExploreArtistUiState(),
    val newAlbumUiState: NewAlbumViewUiState = NewAlbumViewUiState(),
    val newArtisUiState: NewArtistViewUiState = NewArtistViewUiState(),
    val createPlaylistUiState: CreatePlaylistViewUiState = CreatePlaylistViewUiState(),
    val viewSongArtistSongId: Long = -1,
    val player: PlayerUiState = PlayerUiState(),
)

data class HomeViewUiState(
    val isOpen: Boolean = false,
    val songId: Long = -1,
    val type: ViewDataType = ViewDataType.PLAYLIST,
)

data class ExploreArtistUiState(
    val isOpen: Boolean = false,
    val artistId: Long = -1,
)

data class NewAlbumViewUiState(
    val isOpen: Boolean = false,
)

data class NewArtistViewUiState(
    val isOpen: Boolean = false,
)

data class CreatePlaylistViewUiState(
    val isOpen: Boolean = false,
    val playlistId: Long = -1,
)
