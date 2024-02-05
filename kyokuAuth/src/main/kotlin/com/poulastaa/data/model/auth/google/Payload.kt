package com.poulastaa.data.model.auth.google

data class Payload(
    val sub: String,
    val userName: String,
    val email: String,
    val pictureUrl: String
)
