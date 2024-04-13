package com.poulastaa.kyoku.data.model.api.service.playlist

import kotlinx.serialization.Serializable

@Serializable
data class AddSongToPlaylistReq(
    val songId: Long = 0,
    val isAddToFavourite: Boolean = false,
    val listOfPlaylistId: List<Long> = emptyList()
)
