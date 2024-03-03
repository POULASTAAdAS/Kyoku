package com.poulastaa.data.model.home

import com.poulastaa.data.model.common.ResponseArtist
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtistsPreview(
    val artist:ResponseArtist = ResponseArtist(),
    val listOfSongs:List<HomeResponseSong> = emptyList()
)
