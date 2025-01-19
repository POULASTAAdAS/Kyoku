package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseGenre(
    val id: Int,
    val name: String,
    val cover: String? = null,
)
