package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val coverImage: String?,
    val followers: Long,
    val monthlyListeners: Long,
    val birthDate: String?,
    val biography: String?,
)
