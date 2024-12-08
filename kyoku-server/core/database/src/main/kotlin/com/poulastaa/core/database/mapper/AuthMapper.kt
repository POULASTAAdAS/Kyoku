package com.poulastaa.core.database.mapper

import com.poulastaa.core.database.dao.UserDao
import com.poulastaa.core.domain.model.DBUserDto

fun UserDao.toDbUserDto() = DBUserDto(
    id = id.value,
    email = email,
    userName = username,
    passwordHash = passwordHash,
    profilePicUrl = profilePicUrl,
    countryCode = countryId,
    bDate = bDate
)