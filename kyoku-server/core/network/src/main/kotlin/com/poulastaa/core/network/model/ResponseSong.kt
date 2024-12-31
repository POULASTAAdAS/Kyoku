package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseSong(
    val id: Long,
    val title: String,
    val poster: String?,
    val masterPlaylist: String,
    val artist: List<ResponseArtist>,
    val album: ResponseAlbum,
    val info: ResponseSongInfo,
    val genre: ResponseGenre?,
)
