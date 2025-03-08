package com.poulastaa.view.network.model

import com.poulastaa.core.domain.model.SongId
import kotlinx.serialization.Serializable

@Serializable
data class ResponseDetailPrevSong(
    val id: SongId = -1,
    val title: String = "",
    val poster: String? = null,
    val artists: String? = null,
    val releaseYear: Int = -1,
)
