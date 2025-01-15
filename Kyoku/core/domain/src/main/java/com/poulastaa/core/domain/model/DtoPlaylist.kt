package com.poulastaa.core.domain.model

typealias PlaylistId = Long

data class DtoPlaylist(
    val id: PlaylistId = -1,
    val name: String = "",
    val visibilityState:Boolean,
    val popularity: Long = -1,
)
