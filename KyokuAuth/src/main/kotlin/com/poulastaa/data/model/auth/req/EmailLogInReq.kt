package com.poulastaa.data.model.auth.req

import kotlinx.serialization.Serializable

@Serializable
data class EmailLogInReq(
    val email: String,
    val password: String,
) : AuthReqBaseModel
