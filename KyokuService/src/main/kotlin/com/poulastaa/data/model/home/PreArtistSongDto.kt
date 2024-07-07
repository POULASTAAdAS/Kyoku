package com.poulastaa.data.model.home

import com.poulastaa.data.model.ArtistDto
import kotlinx.serialization.Serializable

@Serializable
data class PreArtistSongDto(
    val artist: ArtistDto,
    val songs: List<PrevSongDetailDto>,
)
