package com.poulastaa.kyoku.data.model.api.auth.passkey

data class PasskeyJson(
    val type: String,
    val challenge: String,
    val req: String,
    val token:String
)
