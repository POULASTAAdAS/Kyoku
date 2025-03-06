package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.DtoPrevSong

data class DtoViewArtisPayload(
    val artist: DtoViewArtist = DtoViewArtist(),
    val mostPopularSongs: List<DtoPrevSong> = emptyList(),
)