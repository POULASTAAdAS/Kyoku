package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityArtist(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    @ColumnInfo(index = true)
    val name: String,
    val coverImage: String? = null,
    val popularity: Long = 0,
)
