package com.poulastaa.auth.network.req

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpReq(
    val type: String = "com.poulastaa.data.model.auth.req.EmailSignUpReq",
    var email: String,
    val password: String,
    val userName: String,
    val countryCode: String,
)