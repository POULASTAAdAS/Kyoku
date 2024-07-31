package com.poulastaa.data.model.auth.response.login

import kotlinx.serialization.Serializable

@Serializable
data class PreArtistSongDto(
    val artist: ArtistDto,
    val songs: List<PrevSongDetailDto>,
)