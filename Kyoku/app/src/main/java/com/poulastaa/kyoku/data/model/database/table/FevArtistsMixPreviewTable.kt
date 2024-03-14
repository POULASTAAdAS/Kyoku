package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "FevArtistsMixPreviewTable")
data class FevArtistsMixPreviewTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val artist: String = "",
    val coverImage: String = ""
)
