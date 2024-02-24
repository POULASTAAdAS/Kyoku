package com.poulastaa.kyoku.data.model.api.req

import kotlinx.serialization.Serializable

@Serializable
data class CreatePasskeyUserReq(
    val id: String,
    val email: String,
    val userName: String,
    val token: String,
    val countryCode: String
)
