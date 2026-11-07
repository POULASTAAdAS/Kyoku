package com.poulastaa.kyoku.search.domain.model.dto

import com.poulastaa.kyoku.search.utils.SongId

data class DtoTrackQuery(
    val position: Int,
    val title: String,
    val artists: List<String>,
    val album: String?,
)

data class DtoSongMatch(
    val position: Int,
    val songId: SongId,
    val score: Double,
)
