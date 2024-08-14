package com.poulastaa.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistWithSongReq(
    val name: String,
    val songId: Long,
)
