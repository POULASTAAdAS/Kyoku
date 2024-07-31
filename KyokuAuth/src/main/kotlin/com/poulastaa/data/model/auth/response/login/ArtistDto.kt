package com.poulastaa.data.model.auth.response.login

import kotlinx.serialization.Serializable

@Serializable
data class ArtistDto(
    val id: Long = -1,
    val name: String = "",
    val coverImage: String? = null,
)
