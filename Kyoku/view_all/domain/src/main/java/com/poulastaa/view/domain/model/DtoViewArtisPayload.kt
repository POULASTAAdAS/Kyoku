package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.DtoDetailedPrevSong

data class DtoViewArtisPayload<T>(
    val artist: T,
    val mostPopularSongs: List<DtoDetailedPrevSong> = emptyList(),
)