package com.poulastaa.kyoku.data.model.api.req

import kotlinx.serialization.Serializable

@Serializable
data class EmailLogInReq(
    val type:String,
    val authType:String,
    val email: String,
    val password: String
)