package com.poulastaa.kyoku.data.model.api.req

import kotlinx.serialization.Serializable

@Serializable
data class GoogleAuthReq(
    val type:String,
    val authType:String,
    val tokenId: String,
    val countryCode: String
)
