package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.SongDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.auth.Email

interface LocalCoreCacheDatasource {
    fun cacheUsersByEmail(email: String, type: UserType): DBUserDto?
    fun setUserByEmail(key: Email, type: UserType, value: DBUserDto)

    fun getSongByTitle(list: List<String>): List<SongDto>
}