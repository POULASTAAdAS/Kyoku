package com.poulastaa.setup.domain.model

data class DtoFullPlaylist(
    val playlist: DtoPlaylist,
    val songs: List<DtoSong>,
)
