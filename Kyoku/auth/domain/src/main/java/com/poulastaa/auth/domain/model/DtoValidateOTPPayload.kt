package com.poulastaa.auth.domain.model

import com.poulastaa.core.domain.utils.JWTToken

data class DtoValidateOTPPayload(
    val status: DtoValidateOTPStatus,
    val token: JWTToken,
)
