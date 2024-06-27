package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistDto(
    val id: Long = -1,
    val name: String = "",
    val listOfSong: List<SongDto> = emptyList(),
)
