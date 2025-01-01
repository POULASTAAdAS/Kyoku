package com.poulastaa.core.domain.model

data class DtoSong(
    val id: Long,
    val title: String,
    val poster: String?,
    val masterPlaylist: String,
    val artist: List<DtoArtist>,
    val album: DtoAlbum?,
    val info: DtoSongInfo,
    val genre: DtoGenre?,
)
