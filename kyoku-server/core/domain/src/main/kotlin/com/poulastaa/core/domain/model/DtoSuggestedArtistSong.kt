package com.poulastaa.core.domain.model

data class DtoSuggestedArtistSong(
    val artist: DtoPrevArtist,
    val prevSong: List<DtoPrevSong>
)
