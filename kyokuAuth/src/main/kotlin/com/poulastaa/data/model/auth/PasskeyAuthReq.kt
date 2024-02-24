package com.poulastaa.data.model.auth

import com.poulastaa.utils.Constants.AUTH_TYPE_PASSKEY
import kotlinx.serialization.Serializable

@Serializable
data class PasskeyAuthReq(
    val email: String,
    val displayName: String
) : AuthReqBaseModel(authType = AUTH_TYPE_PASSKEY)
