package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val type: HomeType = HomeType.NEW_USER_REQ,
    val isOldEnough: Boolean = false,
    val fevArtistsMixPreview: List<FevArtistsMixPreview> = emptyList(), //for new user
    val albumPreview: ResponseAlbumPreview = ResponseAlbumPreview(),
    val artistsPreview: List<ResponseArtistsPreview> = emptyList(),
    val pinned: List<Pinned> = emptyList(),

    /** Sent when there are enough songs to calculate.
     ** Dependent on isOldEnough from HomeReq */
    val dailyMixPreview: DailyMixPreview = DailyMixPreview(),

    // only for login users
    val playlist: List<ResponsePlaylist> = emptyList(),
    val favourites: Favourites = Favourites(),
    val albums: List<ResponseAlbum> = emptyList(),
    val historyPreview: List<SongPreview> = emptyList()
)
