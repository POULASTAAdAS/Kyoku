package com.poulastaa.data.model.artist

import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.home.HomeResponseStatus
import com.poulastaa.data.model.home.SongPreview
import kotlinx.serialization.Serializable

@Serializable
data class ArtistMostPopularSongRes(
    val status: HomeResponseStatus = HomeResponseStatus.FAILURE,
    val points: Long = 0,
    val listOfSong: List<SongPreview> = emptyList()
)
