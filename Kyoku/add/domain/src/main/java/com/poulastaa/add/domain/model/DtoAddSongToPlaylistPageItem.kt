package com.poulastaa.add.domain.model

data class DtoAddSongToPlaylistPageItem(
    val type: DtoAddSongToPlaylistPageType,
    val data: List<DtoAddToPlaylistItem> = emptyList(),
)