package com.example.data.model.api

import kotlinx.serialization.Serializable

@Serializable
data class UnAuthorisedResponse(
    val message: String
)
