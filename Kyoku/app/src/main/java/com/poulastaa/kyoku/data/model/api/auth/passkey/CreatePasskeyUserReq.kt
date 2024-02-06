package com.poulastaa.kyoku.data.model.api.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
data class CreatePasskeyUserReq(
    val id: String,
    val email: String,
    val userName: String,
    val token: String
)
