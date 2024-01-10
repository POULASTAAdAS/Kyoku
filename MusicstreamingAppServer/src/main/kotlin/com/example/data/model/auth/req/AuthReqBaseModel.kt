package com.example.data.model.auth.req

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthReqBaseModel(
    val authType: String
)