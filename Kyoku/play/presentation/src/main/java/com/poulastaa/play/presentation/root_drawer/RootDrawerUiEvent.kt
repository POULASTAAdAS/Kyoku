package com.poulastaa.play.presentation.root_drawer

import com.poulastaa.core.domain.ScreenEnum
import com.poulastaa.play.domain.SaveScreen
import com.poulastaa.play.presentation.view.components.ViewDataType

sealed interface RootDrawerUiEvent {
    data object OnDrawerToggle : RootDrawerUiEvent
    data class Navigate(val screen: ScreenEnum) : RootDrawerUiEvent

    data class SaveScreenToggle(val screen: SaveScreen) : RootDrawerUiEvent

    data class AddSongToPlaylist(val id: Long) : RootDrawerUiEvent
    data object OnAddSongToPlaylistCancel : RootDrawerUiEvent

    data class View(val id: Long, val type: ViewDataType) : RootDrawerUiEvent
    data object OnViewCancel : RootDrawerUiEvent

    data class OnExploreArtistOpen(val id: Long) : RootDrawerUiEvent
    data object OnExploreArtistCancel : RootDrawerUiEvent

    data object NewAlbum : RootDrawerUiEvent
    data object NewAlbumCancel : RootDrawerUiEvent
}