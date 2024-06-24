package com.poulastaa.data.model.auth.res

data class Payload(
    val sub: String,
    val userName: String,
    val email: String,
    val pictureUrl: String,
)