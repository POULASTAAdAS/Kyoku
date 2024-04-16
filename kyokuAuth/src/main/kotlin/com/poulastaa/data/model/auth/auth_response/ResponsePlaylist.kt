package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class ResponsePlaylist(
    val id: Long = -1,
    val name: String = "",
    val listOfSongs: List<ResponseSong> = emptyList()
)
