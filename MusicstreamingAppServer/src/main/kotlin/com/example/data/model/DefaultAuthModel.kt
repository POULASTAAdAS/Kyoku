package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class DefaultAuthModel(
    val authType: String
)