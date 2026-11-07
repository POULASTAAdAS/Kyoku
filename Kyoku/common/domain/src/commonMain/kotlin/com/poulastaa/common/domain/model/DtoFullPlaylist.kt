package com.poulastaa.common.domain.model

data class DtoFullPlaylist(
    val playlist: DtoPlaylist,
    val songs: List<DtoSong>,
)
