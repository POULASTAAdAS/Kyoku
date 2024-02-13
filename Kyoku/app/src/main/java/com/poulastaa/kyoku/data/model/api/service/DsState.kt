package com.poulastaa.kyoku.data.model.api.service

import com.poulastaa.kyoku.data.model.api.auth.AuthType

data class DsState(
    val tokenOrCookie: String = "",
    val refreshToken: String = "",
    val authType: AuthType = AuthType.UN_AUTH,
    val profilePidUri: String = "",
    val userName: String = "",
    val isCookie: Boolean = false
)
