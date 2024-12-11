package com.poulastaa.auth.network.model

import kotlinx.serialization.Serializable

@Serializable
data class UnAuthorizedResponse(
    val message: String = "Invalid Request",
)
