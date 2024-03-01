package com.poulastaa.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class UnAuthorisedResponse(
    val message: String
)
