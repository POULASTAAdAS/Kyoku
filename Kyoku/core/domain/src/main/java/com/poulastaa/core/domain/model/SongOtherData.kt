package com.poulastaa.core.domain.model

data class SongOtherData(
    val otherId: Long,
    val title: String,
    val isPlaylist: Boolean,
    val coverImage: List<String>
)
