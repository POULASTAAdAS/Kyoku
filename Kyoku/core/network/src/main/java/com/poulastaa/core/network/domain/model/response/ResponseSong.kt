package com.poulastaa.core.network.domain.model.response

import com.poulastaa.core.domain.utils.SongId
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSong(
    val songId: SongId,
    val title: String,
    val coverImage: String?,
    val artist: List<ResponseArtist>,
)
