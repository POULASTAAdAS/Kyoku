package com.poulastaa.core.domain.model

data class CreatePlaylistPagingData(
    val id: Long,
    val title: String,
    val coverImage: String,
    val artist: String,
    val expandable: Boolean,
    val isArtist: Boolean
)