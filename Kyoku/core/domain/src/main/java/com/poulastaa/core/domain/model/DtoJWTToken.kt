package com.poulastaa.core.domain.model

import com.poulastaa.core.domain.utils.JWTToken

data class DtoJWTToken(
    val access: JWTToken,
    val refresh: JWTToken,
)
