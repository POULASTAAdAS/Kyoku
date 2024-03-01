package com.poulastaa.domain.repository.users

import com.poulastaa.data.model.utils.User
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus

interface PasskeyAuthUserRepository {
    suspend fun updateBDate(date: Long, userId: String): SetBDateResponseStatus
    suspend fun getCountryId(userId: String): Int?
    suspend fun getUser(userId:String): User?
}