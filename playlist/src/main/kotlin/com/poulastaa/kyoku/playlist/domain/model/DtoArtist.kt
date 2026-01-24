package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.ArtistId
import java.util.*

data class DtoArtist(
    val id: ArtistId = 0,
    val name: String = "",
    private val rawCoverImage: String? = null,
    val followers: Long = 0,
    val birthDate: Date? = null,
    val biography: String? = null,
    val monthlyListeners: Long = 0,
    val albums: List<DtoAlbum> = emptyList(),
    val genres: List<DtoGenre> = emptyList(),
) {
    // TODO: modify rawCoverImage
    val coverImage = rawCoverImage
}
