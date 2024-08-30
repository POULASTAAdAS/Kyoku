package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist
import com.poulastaa.play.presentation.add_as_playlist.PlaylistBottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.home.model.HomeItemBottomSheetUiState
import com.poulastaa.play.presentation.root_drawer.home.model.UiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum

data class HomeUiState(
    val heading: String = "",
    val isDataLoading: Boolean = true,
    val isPlaylistLoaded: Boolean = false,
    val isAlbumLoaded: Boolean = false,
    val isNewUser: Boolean = true,

    val header: String = "",

    val savedPlaylists: List<UiPrevPlaylist> = emptyList(),
    val savedAlbums: List<UiPrevAlbum> = emptyList(),
    val staticData: UiHomeData = UiHomeData(),

    val itemBottomSheetUiState: HomeItemBottomSheetUiState = HomeItemBottomSheetUiState(),
    val playlistBottomSheetUiState: PlaylistBottomSheetUiState = PlaylistBottomSheetUiState(),
) {
    val canShowUi: Boolean
        get() = !isNewUser && !isDataLoading && isPlaylistLoaded && isAlbumLoaded
}

data class HomeAddToPlaylistUiState(
    val isOpen: Boolean = false,
    val songId: Long = -1
)