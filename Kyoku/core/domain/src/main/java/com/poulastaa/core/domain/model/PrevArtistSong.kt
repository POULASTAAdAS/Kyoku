package com.poulastaa.core.domain.model

data class PrevArtistSong(
    val artist: Artist = Artist(),
    val songs: List<PrevSongDetail> = emptyList(),
)
