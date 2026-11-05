package com.poulastaa.common.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.poulastaa.common.database.entity.UserEntity

@Dao
interface AuthDao {
    @Upsert
    suspend fun upsertUser(user: UserEntity)
}
