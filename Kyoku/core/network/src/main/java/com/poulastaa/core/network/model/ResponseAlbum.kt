package com.poulastaa.core.network.model

import com.poulastaa.core.domain.model.AlbumId
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val poster: String? = null,
    val popularity: Long = -1,
)
