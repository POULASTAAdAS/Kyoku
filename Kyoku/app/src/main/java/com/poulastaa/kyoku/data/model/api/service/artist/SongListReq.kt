package com.poulastaa.kyoku.data.model.api.service.artist

import kotlinx.serialization.Serializable

@Serializable
data class SongListReq(
    val listOfSongId: List<Long>
)
