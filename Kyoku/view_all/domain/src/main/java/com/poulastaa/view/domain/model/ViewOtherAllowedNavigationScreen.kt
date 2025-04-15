package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.PlaylistId

sealed interface ViewOtherAllowedNavigationScreen {
    data class Artist(val artistId: ArtistId) : ViewOtherAllowedNavigationScreen
    data class CreatePlaylist(val playlistId: PlaylistId) : ViewOtherAllowedNavigationScreen
}