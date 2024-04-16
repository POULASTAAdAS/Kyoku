package com.poulastaa.kyoku.data.model.api.service.artist

import kotlinx.serialization.Serializable

@Serializable
data class ViewArtist(
    val id: Long,
    val name: String,
    val coverImage: String,
    val points: Long
)