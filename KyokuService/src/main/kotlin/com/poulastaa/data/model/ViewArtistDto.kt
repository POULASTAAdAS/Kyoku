package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ViewArtistDto(
    val followers: Long = -1,
    val artist: ArtistDto = ArtistDto(),
    val listOfSong: List<ViewArtistSongDto> = emptyList(),
)