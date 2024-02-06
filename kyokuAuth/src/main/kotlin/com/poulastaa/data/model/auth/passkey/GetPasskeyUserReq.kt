package com.poulastaa.data.model.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
data class GetPasskeyUserReq(
    val id: String,
    val token: String
)
