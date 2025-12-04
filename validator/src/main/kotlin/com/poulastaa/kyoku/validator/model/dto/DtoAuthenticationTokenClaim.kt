package com.poulastaa.kyoku.validator.model.dto

import com.poulastaa.kyoku.validator.utils.Email

data class DtoAuthenticationTokenClaim(
    val email: Email,
    val userType: UserType,
)