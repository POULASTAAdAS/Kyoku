package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifySong(
    var title: String? = null,
    var album: String? = null
)
