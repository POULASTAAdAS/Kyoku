package com.poulastaa.data.model.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpotifySong(
    var title: String? = null,
    var album: String? = null
)
