package com.poulastaa.core.domain.model

data class DtoPrevPlaylist(
    val id: PlaylistId,
    val title: String,
    val posters: List<String?>,
)
