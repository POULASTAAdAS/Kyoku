package com.poulastaa.data.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class ArtistMostPopularSongReq(
    val id: Long,
    val name: String
)
