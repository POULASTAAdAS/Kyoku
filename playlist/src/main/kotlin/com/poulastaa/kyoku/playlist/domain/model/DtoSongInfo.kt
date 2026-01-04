package com.poulastaa.kyoku.playlist.domain.model

import com.poulastaa.kyoku.playlist.utils.SongId

data class DtoSongInfo(
    val songId: SongId = 0,
    val releaseYear: Int = 0,
    val composer: List<DtoComposer> = emptyList(),
    val popularity: Long = 0,
)
