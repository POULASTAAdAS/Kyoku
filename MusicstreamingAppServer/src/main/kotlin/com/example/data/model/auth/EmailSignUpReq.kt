package com.example.data.model.auth

import com.example.data.model.auth.AuthReqBaseModel
import com.example.util.Constants.AUTH_TYPE_EMAIL_SIGN_UP
import kotlinx.serialization.Serializable
@Serializable
data class EmailSignUpReq(
    val email:String,
    val password: String,
    val userName: String
): AuthReqBaseModel(authType = AUTH_TYPE_EMAIL_SIGN_UP)
