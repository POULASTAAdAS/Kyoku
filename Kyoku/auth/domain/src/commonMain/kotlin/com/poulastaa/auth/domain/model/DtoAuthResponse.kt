package com.poulastaa.auth.domain.model

import com.poulastaa.common.domain.model.DtoTokens
import com.poulastaa.common.domain.model.DtoUser

data class DtoAuthResponse(
    val user: DtoUser,
    val tokens: DtoTokens,
)