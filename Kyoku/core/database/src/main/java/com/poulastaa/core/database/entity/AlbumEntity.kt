package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlbumEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val coverImage: String = "",
    val index: Int = 0,
)
