package com.poulastaa.core.network.model

import com.poulastaa.core.domain.repository.SongId
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePrevSong(
    val id: SongId = -1,
    val title: String = "",
    val poster: String? = null,
)