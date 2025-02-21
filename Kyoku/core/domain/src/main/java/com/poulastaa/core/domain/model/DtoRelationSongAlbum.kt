package com.poulastaa.core.domain.model

data class DtoRelationSongAlbum(
    val albumId: AlbumId,
    val list: List<SongId>,
)
