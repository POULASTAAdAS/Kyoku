package com.poulastaa.data.model.auth.response

data class Payload(
    val sub: String,
    val userName: String,
    val email: String,
    val pictureUrl: String,
)