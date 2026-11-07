package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class AlbumEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val poster: String?,
    val popularity: Long,
)
