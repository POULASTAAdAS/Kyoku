package com.poulastaa.network.model

data class SongOtherDto(
    val otherId: Long = -1,
    val title: String = "",
    val isPlaylist: Boolean,
    val coverImage: List<String> = emptyList()
)
