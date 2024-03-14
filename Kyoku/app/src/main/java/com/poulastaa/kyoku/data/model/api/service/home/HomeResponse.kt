package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val type: HomeType = HomeType.NEW_USER_REQ,
    val fevArtistsMixPreview: List<FevArtistsMixPreview>, //for new user
    val albumPreview: ResponseAlbumPreview,
    val artistsPreview: List<ResponseArtistsPreview>,

    /** Sent when there are enough songs to calculate.
     ** Dependent on isOldEnough from HomeReq */
    val dailyMixPreview: DailyMixPreview,

    // only for login users
    val playlist: List<ResponsePlaylist>,
    val favourites: Favourites
)
