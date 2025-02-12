package com.poulastaa.core.domain.model

data class DtoFullPlaylist(
    val playlist: DtoPlaylist,
    val songs: List<DtoSong>,
)
