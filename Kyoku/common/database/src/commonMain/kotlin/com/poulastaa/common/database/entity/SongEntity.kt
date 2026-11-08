package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val poster: String?,
    val releaseYear: Int?,
    val popularity: Long?,
)
