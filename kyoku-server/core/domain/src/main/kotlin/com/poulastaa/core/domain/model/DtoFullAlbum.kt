package com.poulastaa.core.domain.model

data class DtoFullAlbum(
    val album: DtoAlbum,
    val songs: List<DtoSong>,
)
