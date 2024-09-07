package com.poulastaa.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistPagingDto(
    val id: Long,
    val title: String,
    val artist: String = "",
    val coverImage: String,
    val expandable: Boolean,
    val isArtist: Boolean,
)