package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PlaylistTable")
data class PlaylistTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val points: Int = 0,
    val name: String = "",
)
