package com.poulastaa.kyoku.data.model.api.service.home

import kotlinx.serialization.Serializable

@Serializable
data class FevArtistsMixPreview(
    val coverImage: String,
    val artist: String
)
