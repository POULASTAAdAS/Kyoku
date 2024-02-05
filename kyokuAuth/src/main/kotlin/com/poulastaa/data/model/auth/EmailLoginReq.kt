package com.poulastaa.data.model.auth

import com.poulastaa.utils.Constants.AUTH_TYPE_EMAIL_LOG_IN
import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginReq(
    val email: String,
    val password: String
) : AuthReqBaseModel(authType = AUTH_TYPE_EMAIL_LOG_IN)