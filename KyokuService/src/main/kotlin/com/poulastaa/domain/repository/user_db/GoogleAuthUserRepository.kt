package com.poulastaa.domain.repository.user_db

import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus

interface GoogleAuthUserRepository {
    suspend fun updateBDate(date:Long , sub:String): SetBDateResponseStatus
}