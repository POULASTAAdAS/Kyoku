package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.Constants.POSTER_PARAM

data class DtoSong(
    val id: Long,
    val title: String,
    private val rawPoster: String?,
    val masterPlaylist: String, // todo add converter
    val artist: List<DtoArtist>,
    val album: DtoAlbum?,
    val info: DtoSongInfo,
    val genre: DtoGenre?,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)
    val poster = rawPoster?.let { "$baseUrl${EndPoints.Poster.SongPoster.route}?$POSTER_PARAM=$rawPoster" }
}
