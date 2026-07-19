package com.poulastaa.auth.domain.model

import com.poulastaa.common.domain.model.DtoUser

data class DtoEmailAuthResponse(
    val user: DtoUser,
    val isNewUser: Boolean,
)
