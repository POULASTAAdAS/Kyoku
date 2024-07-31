package com.poulastaa.auth.network.req

import kotlinx.serialization.Serializable


@Serializable
data class EmailLogInReq(
    val type: String = "com.poulastaa.data.model.auth.req.EmailLogInReq",
    val email: String,
    val password: String,
)