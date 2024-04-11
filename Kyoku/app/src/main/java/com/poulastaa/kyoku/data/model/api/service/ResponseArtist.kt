package com.poulastaa.kyoku.data.model.api.service

import kotlinx.serialization.Serializable

@Serializable
data class ResponseArtist(
    val id: Long,
    val name: String,
    val imageUrl: String
)