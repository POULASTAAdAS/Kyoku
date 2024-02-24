package com.poulastaa.data.model.auth

import com.poulastaa.utils.Constants.AUTH_TYPE_GOOGLE
import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthReq(
    val tokenId: String,
    val countryCode: String
): AuthReqBaseModel(authType = AUTH_TYPE_GOOGLE)
