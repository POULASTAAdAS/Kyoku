package com.poulastaa.core.domain.model

data class DtoSong(
    val id: Long,
    val title: String,
    private val rawPoster: String?,
    val masterPlaylist: String,
    val artist: List<DtoArtist>,
    val album: DtoAlbum?,
    val info: DtoSongInfo,
    val genre: DtoGenre?,
) {
    private val baseUrl = System.getenv("BASE_URL").dropLast(1)

    val poster = rawPoster?.let { "$baseUrl${Endpoints.Poster.route}/$rawPoster" }
}
