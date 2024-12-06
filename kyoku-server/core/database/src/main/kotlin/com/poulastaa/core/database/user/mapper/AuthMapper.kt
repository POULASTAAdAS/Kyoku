package com.poulastaa.core.database.user.mapper

import com.poulastaa.core.database.user.dao.UserDao
import com.poulastaa.core.domain.model.DBUserDto

fun UserDao.toDbUserDto() = DBUserDto(
    email = email,
    userName = username,
    passwordHash = passwordHash,
    profilePicUrl = profilePicUrl,
    countryCode = countryId,
    bDate = bDate
)