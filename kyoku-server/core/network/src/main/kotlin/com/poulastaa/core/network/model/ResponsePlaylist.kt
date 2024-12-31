package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponsePlaylist(
    val id: Long = -1,
    val name: String = "",
    val popularity: Long = -1,
)
