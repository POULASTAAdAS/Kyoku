package com.poulastaa.data.model.home

import com.poulastaa.data.model.common.ResponseArtist
import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtistsPreview(
    val artist:ResponseArtist = ResponseArtist(),
    val listOfSongs:List<ArtistSong> = emptyList()
)


@Serializable
data class ArtistSong(
    val songId: Long,
    val title: String,
    val coverImage: String
)