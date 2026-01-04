package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.AlbumId

data class DtoAlbum(
    val id: AlbumId = -1,
    val name: String = "",
    val popularity: Long = 0,
    private val rawPoster: String? = null,
    val artists: List<DtoArtist> = emptyList(),
) {
    // TODO: modify rawPoster
    val poster = rawPoster
}
