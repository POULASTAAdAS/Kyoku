package com.poulastaa.core.domain.model

data class DtoPlaylist(
    val id: Long = -1,
    val name: String = "",
    val popularity: Long = -1,
    val visibilityState: Boolean = false,
)
