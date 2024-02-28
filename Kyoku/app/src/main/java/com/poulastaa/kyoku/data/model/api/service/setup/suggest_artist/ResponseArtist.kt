package com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Int,
    val name: String,
    val imageUrl: String
)