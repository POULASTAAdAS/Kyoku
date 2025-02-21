package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityCountry(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
)
