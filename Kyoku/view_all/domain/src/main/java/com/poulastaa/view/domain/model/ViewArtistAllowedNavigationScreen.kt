package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId

sealed interface ViewArtistAllowedNavigationScreen {
    data class Explore(val artistId: ArtistId) : ViewArtistAllowedNavigationScreen
    data class ViewAlbum(val albumId: AlbumId) : ViewArtistAllowedNavigationScreen
}