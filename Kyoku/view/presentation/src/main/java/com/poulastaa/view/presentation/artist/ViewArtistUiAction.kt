package com.poulastaa.view.presentation.artist

import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.presentation.designsystem.model.PlayType

internal sealed interface ViewArtistUiAction {
    data object OnFollowToggle : ViewArtistUiAction
    data object OnExploreArtist : ViewArtistUiAction
    data class OnPlayAll(val playType: PlayType) : ViewArtistUiAction
    data class OnPlaySong(val songId: SongId) : ViewArtistUiAction
    data class OnSongOptionsToggle(val songId: SongId) : ViewArtistUiAction
    data object OnSongEditModeToggle : ViewArtistUiAction
    data class OnSelectSong(val songId: SongId) : ViewArtistUiAction
}