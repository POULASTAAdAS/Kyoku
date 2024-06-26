package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SonEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val coverImage: String,
    val title: String,
    val releaseDate: String,
    val masterPlaylistUrl: String,
    val lightVibrant: String,
    val darkVibrant: String,
    val mutedSwatch: String,
)
