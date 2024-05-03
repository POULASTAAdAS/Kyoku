package com.poulastaa.kyoku.data.model.api.service.artist

import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import kotlinx.serialization.Serializable

@Serializable
data class SongListResponse(
    val list: List<ResponseSong> = emptyList()
)
