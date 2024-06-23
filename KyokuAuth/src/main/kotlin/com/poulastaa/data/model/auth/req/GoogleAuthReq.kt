package com.poulastaa.data.model.auth.req

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthReq(
    val token: String,
    val countryCode: String
) : AuthReqBaseModel
