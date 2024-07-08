package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PopularSuggestArtistEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val coverImage: String,
)
