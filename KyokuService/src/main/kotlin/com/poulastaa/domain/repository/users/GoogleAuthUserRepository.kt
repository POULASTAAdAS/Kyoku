package com.poulastaa.domain.repository.users

import com.poulastaa.data.model.User
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus

interface GoogleAuthUserRepository {
    suspend fun updateBDate(date: Long, sub: String): SetBDateResponseStatus
    suspend fun getCountryId(sub: String): Int?
    suspend fun getUser(sub:String): User?
}