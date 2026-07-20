package com.poulastaa.kyoku.auth.model.response

data class AuthResponse(
    val user: ResponseUser,
    val isNewUser: Boolean,
)
