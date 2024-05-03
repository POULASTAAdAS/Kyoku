package com.poulastaa.data.model.artist

import com.poulastaa.data.model.common.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SongListResponse(
    val list: List<ResponseSong> = emptyList()
)