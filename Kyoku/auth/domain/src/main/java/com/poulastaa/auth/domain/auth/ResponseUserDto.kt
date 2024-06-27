package com.poulastaa.auth.domain.auth

data class ResponseUser(
    val email: String = "",
    val userName: String = "",
    val profilePic: String = "",
)