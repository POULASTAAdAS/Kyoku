package com.poulastaa.kyoku.gateway.model.dto

import com.poulastaa.kyoku.gateway.model.UserType
import com.poulastaa.kyoku.gateway.utils.Email

data class DtoAuthenticationTokenClaim(
    val email: Email,
    val userType: UserType,
)