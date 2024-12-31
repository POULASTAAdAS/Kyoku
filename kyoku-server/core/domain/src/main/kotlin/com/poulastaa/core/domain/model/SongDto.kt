package com.poulastaa.core.domain.model

data class SongDto(
    val id: Long,
    val title: String,
    val poster: String?,
    val masterPlaylist: String,
    val artist: List<ArtistDto>,
    val album: AlbumDto,
    val info: SongInfoDto,
    val genre: GenreDto?,
)
