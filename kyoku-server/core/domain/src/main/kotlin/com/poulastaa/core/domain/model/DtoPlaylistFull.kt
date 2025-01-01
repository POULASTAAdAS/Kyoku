package com.poulastaa.core.domain.model

data class DtoPlaylistFull(
    val playlist: DtoPlaylist = DtoPlaylist(),
    val listOfSong: List<DtoSong> = emptyList(),
)
