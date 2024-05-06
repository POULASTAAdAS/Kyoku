package com.poulastaa.kyoku.data.model.api.service.song

import kotlinx.serialization.Serializable

@Serializable
data class SongAdditionalInfo(
    val artist: List<PlayingSongArtist> = emptyList(),
    val album: PlayingSongAlbumResponse = PlayingSongAlbumResponse(),
    val releaseYear: String = ""
)

@Serializable
data class PlayingSongAlbumResponse(
    val albumId: Long = -1,
    val name: String = ""
)

@Serializable
data class PlayingSongArtist(
    val artistId: Long = -1,
    val name: String = "",
    val coverImage: String = "",
    val listened: Long = -1
)