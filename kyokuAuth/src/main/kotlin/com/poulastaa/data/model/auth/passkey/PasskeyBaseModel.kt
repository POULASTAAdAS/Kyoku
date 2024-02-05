package com.poulastaa.data.model.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
sealed class PasskeyBaseModel(
    val type: String
)