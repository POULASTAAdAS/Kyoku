package com.poulastaa.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
sealed class AuthReqBaseModel(
    val authType: String
)