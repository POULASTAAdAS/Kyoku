package com.poulastaa.kyoku.auth.model.dto

data class GoogleAuthPayload(
    val sub: String,
    val name: String,
    val displayName: String,
    val email: String,
    val picture: String,
)
