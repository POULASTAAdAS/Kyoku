package com.poulastaa.auth.domain.model

import com.poulastaa.core.domain.model.DtoJWTToken

data class DtoGoogleAuth(
    val user: DtoResponseUser,
    val token: DtoJWTToken,
)