package com.poulastaa.core.data.mapper

import com.poulastaa.core.data.model.SerializedUser
import com.poulastaa.core.domain.model.DtoUser
import java.time.LocalDate

internal fun DtoUser.toSerializedUser() = SerializedUser(
    username = this.username,
    email = this.email,
    profileUrl = this.profileUrl,
    type = this.type,
    bDate = this.bDate.toString()
)

internal fun SerializedUser.toDtoUser() = DtoUser(
    username = this.username,
    email = this.email,
    profileUrl = this.profileUrl,
    type = this.type,
    bDate = this.bDate?.let { LocalDate.parse(this.bDate) }
)