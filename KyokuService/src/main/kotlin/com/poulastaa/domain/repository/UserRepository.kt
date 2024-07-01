package com.poulastaa.domain.repository

import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.UserResult
import com.poulastaa.domain.model.UserType

interface UserRepository {
    suspend fun getUserOnPayload(payload: ReqUserPayload): UserResult?

    suspend fun createUserPlaylist(
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
}