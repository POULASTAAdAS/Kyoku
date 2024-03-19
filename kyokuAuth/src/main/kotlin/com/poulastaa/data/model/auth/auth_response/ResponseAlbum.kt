package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbum(
    val name: String,
    val listOfSongs: List<ResponseSong>
)
