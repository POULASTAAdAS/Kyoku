package com.poulastaa.add.domain.model

data class DtoAddToPlaylistItem(
    val id: Long = -1,
    val title: String = "",
    val poster: List<String> = emptyList(),
    val artist: String? = null,
    val numbers: Long = 0,
    val type: DtoAddToPlaylistItemType,
)