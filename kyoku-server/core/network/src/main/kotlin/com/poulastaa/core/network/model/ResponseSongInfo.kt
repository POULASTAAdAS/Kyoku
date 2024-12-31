package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseSongInfo(
    val id: Long,
    val releaseYear: Int,
    val composer: String?,
    val popularity: Long,
)
