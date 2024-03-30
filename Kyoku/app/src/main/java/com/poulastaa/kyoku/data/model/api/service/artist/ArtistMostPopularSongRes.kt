package com.poulastaa.kyoku.data.model.api.service.artist

import com.poulastaa.kyoku.data.model.api.service.home.HomeResponseStatus
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import kotlinx.serialization.Serializable

@Serializable
data class ArtistMostPopularSongRes(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val points: Long = 0,
    val listOfSong: List<SongPreview> = emptyList()
)
