package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.ArtistId

data class DtoComposer(
    val id: ArtistId = 0,
    val name: String = "",
    val rawCoverImage: String? = null,
    val followers: Long = 0,
) {
    // TODO: modify rawCoverImage
    val coverImage = rawCoverImage
}
