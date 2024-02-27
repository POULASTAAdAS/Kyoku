package com.poulastaa.kyoku.data.model.api.auth.email

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpReq(
    val type:String,
    val authType:String,
    val email:String,
    val password: String,
    val userName: String,
    val countryCode: String
)
