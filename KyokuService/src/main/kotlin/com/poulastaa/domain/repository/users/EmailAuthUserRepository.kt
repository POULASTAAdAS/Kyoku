package com.poulastaa.domain.repository.users

import com.poulastaa.data.model.User
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus

interface EmailAuthUserRepository {
    suspend fun updateBDate(date: Long, email: String): SetBDateResponseStatus
    suspend fun getCountryId(email: String): Int?
    suspend fun getUser(email:String): User?
}