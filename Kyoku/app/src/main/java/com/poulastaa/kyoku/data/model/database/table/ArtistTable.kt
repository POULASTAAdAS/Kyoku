package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistTable")
data class ArtistTable(
    @PrimaryKey
    val artistId: Long = 0,
    val name: String = "",
    val coverImage: String = ""
)
