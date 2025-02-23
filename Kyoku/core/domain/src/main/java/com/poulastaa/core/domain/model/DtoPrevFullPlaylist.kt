package com.poulastaa.core.domain.model

data class DtoPrevFullPlaylist(
    val playlist: DtoPlaylist,
    val list: List<DtoDetailedPrevSong>,
)
