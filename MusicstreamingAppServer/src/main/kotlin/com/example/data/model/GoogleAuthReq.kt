package com.example.data.model

import com.example.util.Constants.AUTH_TYPE_GOOGLE
import kotlinx.serialization.Serializable
@Serializable
data class GoogleAuthReq(
    val tokenId: String
): AuthReqBaseModel(authType = AUTH_TYPE_GOOGLE)
