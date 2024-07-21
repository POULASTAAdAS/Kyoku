package com.poulastaa.play.presentation.root_drawer.home

import com.poulastaa.play.presentation.root_drawer.home.model.UiHomeData
import com.poulastaa.play.presentation.root_drawer.home.model.UiPrevAlbum
import com.poulastaa.core.presentation.ui.model.UiPrevPlaylist

data class HomeUiState(
    val heading: String = "",
    val isDataLoading: Boolean = true,
    val isNewUser: Boolean = true,

    val header: String = "",

    val savedPlaylists: List<UiPrevPlaylist> = emptyList(),
    val savedAlbums: List<UiPrevAlbum> = emptyList(),
    val staticData: UiHomeData = UiHomeData(),
) {
    val canShowUi: Boolean
        get() = !isNewUser && !isDataLoading
}