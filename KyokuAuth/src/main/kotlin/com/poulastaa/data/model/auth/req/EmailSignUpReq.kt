package com.poulastaa.data.model.auth.req

import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpReq(
    var email: String,
    val password: String,
    val userName: String,
    val countryCode: String,
) : AuthReqBaseModel
