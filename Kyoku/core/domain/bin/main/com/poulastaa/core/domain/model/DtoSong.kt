package com.poulastaa.core.domain.model

typealias SongId = Long

data class DtoSong(
    val id: SongId,
    val title: String,
    val poster: String?,
    val masterPlaylist: String,
    val artist: List<DtoArtist>,
    val album: DtoAlbum?,
    val info: DtoSongInfo,
    val genre: DtoGenre?,
)
