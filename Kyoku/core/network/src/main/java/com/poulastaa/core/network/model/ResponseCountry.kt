package com.poulastaa.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseCountry(
    val id: Int,
    val name: String,
)
