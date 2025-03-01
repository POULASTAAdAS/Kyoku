package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityGenre(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val cover: String? = null,
)
