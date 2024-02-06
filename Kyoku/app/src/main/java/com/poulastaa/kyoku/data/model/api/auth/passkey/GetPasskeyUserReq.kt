package com.poulastaa.kyoku.data.model.api.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
data class GetPasskeyUserReq(
    val id: String,
    val token: String
)
