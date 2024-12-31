package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.dao.DaoUser
import com.poulastaa.core.domain.model.DBUserDto

fun DaoUser.toDbUserDto() = DBUserDto(
    id = id.value,
    email = email,
    userName = username,
    passwordHash = passwordHash,
    profilePicUrl = profilePicUrl,
    countryCode = countryId,
    bDate = bDate
)