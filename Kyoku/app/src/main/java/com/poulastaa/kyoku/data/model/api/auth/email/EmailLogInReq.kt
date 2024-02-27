package com.poulastaa.kyoku.data.model.api.auth.email

import kotlinx.serialization.Serializable

@Serializable
data class EmailLogInReq(
    val type:String,
    val authType:String,
    val email: String,
    val password: String
)