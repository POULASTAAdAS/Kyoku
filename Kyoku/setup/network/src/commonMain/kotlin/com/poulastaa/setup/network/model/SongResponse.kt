package com.poulastaa.setup.network.model

import com.poulastaa.setup.domain.model.DtoSong
import kotlinx.serialization.Serializable

@Serializable
data class SongResponse(
    val songId: Long,
    val title: String,
    val poster: String? = null,
    val artists: List<SongArtistResponse> = emptyList(),
) {
    fun toDto() = DtoSong(
        id = songId,
        title = title,
        poster = poster,
        artists = artists.map { it.name },
    )
}

@Serializable
data class SongArtistResponse(
    val name: String,
)
