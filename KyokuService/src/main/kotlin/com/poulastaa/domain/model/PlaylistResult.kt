package com.poulastaa.domain.model

data class PlaylistResult(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<SongWithArtistResult> = emptyList(),
)
