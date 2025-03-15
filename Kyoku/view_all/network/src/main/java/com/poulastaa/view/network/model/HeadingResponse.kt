package com.poulastaa.view.network.model

import kotlinx.serialization.Serializable

@Serializable
data class HeadingResponse(
    val type: ViewTypeResponse,
    val id: Long,
    val name: String,
    val poster: String,
)