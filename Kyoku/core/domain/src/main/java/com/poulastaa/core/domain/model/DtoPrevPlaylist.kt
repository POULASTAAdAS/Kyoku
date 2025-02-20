package com.poulastaa.core.domain.model

data class DtoPrevPlaylist(
    val playlist: DtoPlaylist,
    val list: List<DtoDetailedPrevSong>,
)
