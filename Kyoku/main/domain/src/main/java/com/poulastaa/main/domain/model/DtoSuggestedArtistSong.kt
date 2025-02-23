package com.poulastaa.main.domain.model

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong

data class DtoSuggestedArtistSong(
    val artist: DtoPrevArtist,
    val prevSong: List<DtoPrevSong>,
)