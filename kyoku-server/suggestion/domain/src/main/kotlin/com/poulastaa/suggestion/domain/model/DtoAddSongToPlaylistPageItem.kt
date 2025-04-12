package com.poulastaa.suggestion.domain.model

data class DtoAddSongToPlaylistPageItem(
    val type: DtoAddSongToPlaylistPageType,
    val data: List<DtoAddSongToPlaylistItem> = emptyList(),
)