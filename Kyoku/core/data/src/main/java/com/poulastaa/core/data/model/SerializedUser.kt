package com.poulastaa.core.data.model

import com.poulastaa.core.domain.model.DtoUserType
import com.poulastaa.core.domain.utils.Email
import com.poulastaa.core.domain.utils.Username
import java.time.LocalDateTime
import java.time.ZoneOffset

data class SerializedUser(
    val username: Username,
    val email: Email,
    val profileUrl: String? = null,
    val type: DtoUserType,
    val bDate: String? = null,
)
