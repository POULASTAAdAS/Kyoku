package com.poulastaa.suggestion.network.domain

import com.poulastaa.core.domain.repository.AlbumId
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePrevAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val poster: String? = null,
)
