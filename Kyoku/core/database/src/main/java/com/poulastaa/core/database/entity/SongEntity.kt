package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SongEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val coverImage: String,
    val title: String,
    val artistName: String,
    val releaseYear: String,
    val masterPlaylistUrl: String,
    val primary: String,
    val background: String,
    val onBackground: String,
)
