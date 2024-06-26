package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArtistEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val coverImage: String,
    val name: String,
)
