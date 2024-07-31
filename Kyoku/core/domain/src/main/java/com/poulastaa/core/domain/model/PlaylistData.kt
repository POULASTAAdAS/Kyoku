package com.poulastaa.core.domain.model

data class PlaylistData(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<Song> = emptyList(),
)