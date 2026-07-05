package com.poulastaa.auth.data

import com.poulastaa.auth.domain.AuthLocalDatasource
import org.koin.core.annotation.Single

@Single(binds = [AuthLocalDatasource::class])
class RoomDatasource : AuthLocalDatasource {
    override suspend fun saveUser() {

    }
}