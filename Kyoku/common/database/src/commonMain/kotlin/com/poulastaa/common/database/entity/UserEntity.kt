package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: Int = SINGLE_ROW_ID,
    val userId: Long,
    val username: String,
    val email: String,
    val profilePictureUrl: String?,
)
