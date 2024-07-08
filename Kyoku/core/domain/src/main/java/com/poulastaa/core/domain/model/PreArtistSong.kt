package com.poulastaa.core.domain.model

data class PreArtistSong(
    val artist: Artist = Artist(),
    val songs: List<PrevSongDetail> = emptyList(),
)
