package com.poulastaa.data.model.artist

import kotlinx.serialization.Serializable

@Serializable
data class SongListReq(
    val listOfSongId: List<Long>
)
