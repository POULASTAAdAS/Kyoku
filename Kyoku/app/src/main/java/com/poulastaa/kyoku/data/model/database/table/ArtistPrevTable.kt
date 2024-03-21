package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArtistPrevTable")
data class ArtistPrevTable(
    @PrimaryKey
    val id: Int = 0,
    val name: String = "",
    val imageUrl: String = ""
)
