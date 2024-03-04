package com.poulastaa.data.model.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse( // todo change for preview send only what needed
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val type: HomeType = HomeType.NEW_USER_REQ,
    val fevArtistsMix: List<FevArtistsMixPreview> = emptyList(),
    val album: ResponseAlbumPreview = ResponseAlbumPreview(),
    val artists: List<ResponseArtistsPreview> = emptyList(),

    /** Send when there are enough songs to calculate.
     ** Dependent on isOldEnough from HomeReq */
    val dailyMix: DailyMixPreview = DailyMixPreview(),

    // only for login users
    val playlist: List<ResponsePlaylist> = emptyList(),
    val favourites: Favourites = Favourites()
)
