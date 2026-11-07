package com.poulastaa.setup.domain.model

data class DtoPlaylist(
    val id: Long,
    val name: String,
    val popularity: Long = 0,
    val visibility: DtoPlaylistVisibility = DtoPlaylistVisibility.PUBLIC,
)

enum class DtoPlaylistVisibility {
    PUBLIC,
    PRIVATE,
}
