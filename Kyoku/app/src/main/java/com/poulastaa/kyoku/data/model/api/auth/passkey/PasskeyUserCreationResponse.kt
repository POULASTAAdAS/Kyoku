package com.poulastaa.kyoku.data.model.api.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
data class PasskeyUserCreationResponse(
    val status: Boolean
)
