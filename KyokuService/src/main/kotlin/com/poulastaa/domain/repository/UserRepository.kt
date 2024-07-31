package com.poulastaa.domain.repository

import com.poulastaa.data.model.LogInDto
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType

typealias UserId = Long

interface UserRepository {
    suspend fun getUserOnPayload(payload: ReqUserPayload): UserResult?
    suspend fun getUserOnEmail(
        email: String,
        userType: UserType,
    ): UserResult?

    fun createUserPlaylist(
        userId: Long,
        userType: UserType,
        playlistId: Long,
        songIdList: List<Long>,
    )

    suspend fun updateBDate(
        userId: Long,
        bDate: Long,
        userType: UserType,
    ): Boolean

    suspend fun storeGenre(
        userId: Long,
        userType: UserType,
        idList: List<Int>,
    )

    suspend fun storeArtist(
        userId: Long,
        userType: UserType,
        idList: List<Long>,
    )

    suspend fun getUserData(
        userType: UserType,
        email: String,
    ): Pair<LogInDto, UserId>
}