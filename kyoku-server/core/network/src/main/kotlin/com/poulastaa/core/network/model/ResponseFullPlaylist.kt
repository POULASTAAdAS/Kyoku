package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseFullPlaylist(
    val playlist: ResponsePlaylist,
    val listOfSong: List<ResponseSong> = emptyList(),
)
