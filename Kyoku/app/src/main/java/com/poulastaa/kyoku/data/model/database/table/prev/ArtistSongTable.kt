package com.poulastaa.kyoku.data.model.database.table.prev

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistSongTable")
data class ArtistSongTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long,
    val title: String,
    val coverImage: String
)
