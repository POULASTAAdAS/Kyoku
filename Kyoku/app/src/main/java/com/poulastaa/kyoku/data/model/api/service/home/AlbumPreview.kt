package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class AlbumPreview(
    val id: Long = 0,
    val name: String = "",
    val coverImage: String = ""
)
