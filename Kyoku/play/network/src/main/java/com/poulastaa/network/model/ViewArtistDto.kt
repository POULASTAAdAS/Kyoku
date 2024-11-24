package com.poulastaa.network.model

import com.poulastaa.core.data.model.ArtistDto
import kotlinx.serialization.Serializable

@Serializable
data class ViewArtistDto(
    val followers: Long,
    val artist: ArtistDto,
    val listOfSong: List<ViewArtistSongDto>,
)
