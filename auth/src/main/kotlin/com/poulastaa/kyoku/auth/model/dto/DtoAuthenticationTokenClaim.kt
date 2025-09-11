package com.poulastaa.kyoku.auth.model.dto

import com.poulastaa.kyoku.auth.utils.Email

data class DtoAuthenticationTokenClaim(
    val email: Email,
    val userType: UserType,
)
