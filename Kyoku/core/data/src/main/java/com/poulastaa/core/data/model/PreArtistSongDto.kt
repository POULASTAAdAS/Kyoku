package com.poulastaa.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PreArtistSongDto(
    val artist: ArtistDto,
    val songs: List<PrevSongDetailDto>,
)
