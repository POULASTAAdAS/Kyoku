package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPreview(
    val name: String,
    val coverImage: String,
    val title: String,
    val artist: String
)
