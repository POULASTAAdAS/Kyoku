package com.poulastaa.kyoku.data.model.api.service.artist

import kotlinx.serialization.Serializable

@Serializable
data class ArtistAlbum(
    val id: Long = 0,
    val name: String = "",
    val coverImage: String = "",
    val year: String = ""
)
