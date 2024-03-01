package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val type: HomeType = HomeType.NEW_USER_REQ,
    val fevArtistsMix: FevArtistsMixPreview = FevArtistsMixPreview(),
    val album: List<ResponseAlbumPreview> = emptyList(),
    val artists: List<ResponseArtistsPreview> = emptyList(),

    // only for login users
    val playlist: List<ResponsePlaylist> = emptyList(),
    val favourites: Favourites = Favourites()
)
