package com.poulastaa.view.presentation.artist

import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.presentation.designsystem.model.ItemClickType
import com.poulastaa.core.presentation.designsystem.model.PlayType

internal sealed interface ViewArtistUiAction {
    data object OnFollowToggle : ViewArtistUiAction
    data object OnExploreArtist : ViewArtistUiAction
    data class OnPlayAll(val playType: PlayType) : ViewArtistUiAction
    data class OnSongClick(val clickType: ItemClickType, val songId: SongId) : ViewArtistUiAction
    data class OnSongOptionsToggle(val songId: SongId) : ViewArtistUiAction
}