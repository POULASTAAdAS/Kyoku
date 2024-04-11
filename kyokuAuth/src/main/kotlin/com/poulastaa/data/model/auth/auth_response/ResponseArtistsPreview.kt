package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtistsPreview(
    val artist: ResponseArtist = ResponseArtist(),
    val listOfSongs:List<ArtistSong> = emptyList()
)

@Serializable
data class ArtistSong(
    val songId: Long,
    val title: String,
    val coverImage: String
)
