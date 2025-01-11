package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntitySong(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(index = true)
    val title: String,
    val poster: String? = null,
    val masterPlaylist: String,
)
