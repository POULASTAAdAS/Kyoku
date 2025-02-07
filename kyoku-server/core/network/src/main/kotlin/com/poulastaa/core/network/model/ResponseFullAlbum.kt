package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseFullAlbum(
    val album: ResponseAlbum = ResponseAlbum(),
    val songs: List<ResponseSong> = emptyList(),
)
