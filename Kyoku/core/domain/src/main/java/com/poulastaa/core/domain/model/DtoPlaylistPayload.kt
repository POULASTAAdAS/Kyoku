package com.poulastaa.core.domain.model

data class DtoPlaylistPayload(
    val playlist: DtoPlaylist = DtoPlaylist(visibilityState = false),
    val cover: List<String> = emptyList(),
    val count: Int = 0,
)
