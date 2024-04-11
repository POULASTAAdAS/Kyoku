package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val type: HomeType = HomeType.ALREADY_USER_REQ,
    val isOldEnough: Boolean = false,
    val fevArtistsMixPreview: List<FevArtistsMixPreview> = emptyList(),
    val albumPreview: ResponseAlbumPreview = ResponseAlbumPreview(),
    val artistsPreview: List<ResponseArtistsPreview> = emptyList(),
    val pinned: List<Pinned> = emptyList(),

    /** Send when there are enough songs to calculate.
     ** Dependent on isOldEnough from HomeReq */
    val dailyMixPreview: DailyMixPreview = DailyMixPreview(),

    // only for login users
    val playlist: List<ResponsePlaylist> = emptyList(),
    val favourites: Favourites = Favourites(),
    val albums: List<ResponseAlbum> = emptyList(),
    val historyPreview: List<SongPreview> = emptyList()
)
