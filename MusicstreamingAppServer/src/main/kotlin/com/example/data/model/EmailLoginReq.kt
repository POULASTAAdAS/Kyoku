package com.example.data.model

import com.example.util.Constants.AUTH_TYPE_EMAIL_LOG_IN
import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginReq(
    val email: String,
    val password: String
) : AuthReqBaseModel(authType = AUTH_TYPE_EMAIL_LOG_IN)