package com.example.data.model

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthReqBaseModel(
    val authType: String
)