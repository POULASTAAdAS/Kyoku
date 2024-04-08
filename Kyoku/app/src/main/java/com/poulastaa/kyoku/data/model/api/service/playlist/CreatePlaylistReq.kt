package com.poulastaa.kyoku.data.model.api.service.playlist

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistReq(
    val name: String = "",
    val albumId: Long = -1,
    val listOfSongId: List<Long> = emptyList()
)