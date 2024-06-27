package com.poulastaa.auth.data.model.req

import kotlinx.serialization.Serializable


@Serializable
data class EmailLogInReq(
    val type: String = "com.poulastaa.data.model.auth.req.EmailLogInReq",
    val email: String,
    val password: String,
)