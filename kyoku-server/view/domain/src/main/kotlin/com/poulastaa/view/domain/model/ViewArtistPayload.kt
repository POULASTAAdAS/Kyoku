package com.poulastaa.view.domain.model

import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.core.domain.model.DtoPrevSong

data class ViewArtistPayload(
    val artist: DtoPrevArtist,
    val songs: List<DtoPrevSong>,
)
