package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPreview(
    val name: String,
    val listOfSongs: List<SongPreview> = emptyList()
)
