package com.poulastaa.data.model.auth.auth_response

import kotlinx.serialization.Serializable

@Serializable
data class FevArtistsMixPreview(
    val coverImage: String,
    val artist: String
)
