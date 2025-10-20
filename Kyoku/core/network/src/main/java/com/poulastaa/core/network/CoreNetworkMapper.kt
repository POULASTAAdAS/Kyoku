package com.poulastaa.core.network

import com.poulastaa.core.domain.model.DtoJWTToken
import com.poulastaa.core.network.domain.model.ResponseJWTToken

fun ResponseJWTToken.toDtoJWTToken() = DtoJWTToken(
    access = accessToken,
    refresh = refreshToken
)