package com.poulastaa.kyoku.data.model.api.req

import kotlinx.serialization.Serializable

@Serializable
data class GetPasskeyUserReq(
    val id: String,
    val token: String
)
