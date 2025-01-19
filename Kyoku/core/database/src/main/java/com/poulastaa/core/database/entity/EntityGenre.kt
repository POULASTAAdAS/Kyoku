package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityGenre(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(index = true)
    val name: String,
    val cover: String? = null,
)
