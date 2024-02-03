package com.poulastaa.data.model.auth.req

import com.poulastaa.utils.Constants.AUTH_TYPE_EMAIL_SIGN_UP
import kotlinx.serialization.Serializable
@Serializable
data class EmailSignUpReq(
    val email:String,
    val password: String,
    val userName: String
): AuthReqBaseModel(authType = AUTH_TYPE_EMAIL_SIGN_UP)
