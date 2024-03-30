package com.poulastaa.kyoku.data.model.api.service.artist

import kotlinx.serialization.Serializable

@Serializable
data class ArtistMostPopularSongReq(
    val id: Long,
    val name: String
)
