package com.poulastaa.data.model.auth.passkey

import kotlinx.serialization.Serializable

@Serializable
class CreatePasskeyUserReq(
    val id: String,
    val email: String,
    val userName: String,
    val token:String,
    val countryCode: String
)
