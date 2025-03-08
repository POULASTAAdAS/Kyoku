package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.DtoDetailedPrevSong
import com.poulastaa.core.domain.model.DtoPrevArtist

data class ViewArtistPayload(
    val artist: DtoPrevArtist,
    val songs: List<DtoDetailedPrevSong>,
)
