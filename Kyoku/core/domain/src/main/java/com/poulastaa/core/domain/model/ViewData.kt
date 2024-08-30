package com.poulastaa.core.domain.model

data class ViewData(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<PlaylistSong> = emptyList()
)
