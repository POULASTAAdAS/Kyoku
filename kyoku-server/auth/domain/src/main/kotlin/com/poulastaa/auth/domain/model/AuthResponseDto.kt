package com.poulastaa.auth.domain.model

import com.poulastaa.core.domain.model.DtoUser

data class AuthResponseDto(
    val status: AuthResponseStatusDto = AuthResponseStatusDto.SERVER_ERROR,
    val user: DtoUser = DtoUser(),
)
