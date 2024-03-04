package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val type: HomeType = HomeType.NEW_USER_REQ,
    val fevArtistsMixPreview: List<FevArtistsMixPreview> = emptyList(), //for new user
    val albumPreview: ResponseAlbumPreview = ResponseAlbumPreview(),
    val artistsPreview: List<ResponseArtistsPreview> = emptyList(),

    /** Send when there are enough songs to calculate.
     ** Dependent on isOldEnough from HomeReq */
    val dailyMixPreview: DailyMixPreview = DailyMixPreview(),

    // only for login users
    val playlist: List<ResponsePlaylist> = emptyList(),
    val favourites: Favourites = Favourites()
)
