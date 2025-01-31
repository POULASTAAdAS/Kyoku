package com.poulastaa.core.domain.model

data class DtoFullPlaylist(
    val playlist: DtoPlaylist = DtoPlaylist(),
    val listOfSong: List<DtoSong> = emptyList(),
)
