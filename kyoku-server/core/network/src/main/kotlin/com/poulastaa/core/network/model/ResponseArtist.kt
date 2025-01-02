package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Long,
    val name: String,
    val coverImage: String?,
    val popularity: Long,
    val genre: ResponseGenre,
    val country: ResponseCountry,
)
