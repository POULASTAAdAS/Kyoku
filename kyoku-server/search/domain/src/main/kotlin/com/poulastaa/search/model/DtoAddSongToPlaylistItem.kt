package com.poulastaa.search.model

data class DtoAddSongToPlaylistItem(
    val id: Long = -1,
    val title: String = "",
    val poster: List<String> = emptyList(),
    val artist: String? = null,
    val numbers: Long = 0,
    val type: DtoAddSongToPlaylistItemType,
)