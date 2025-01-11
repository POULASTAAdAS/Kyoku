package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EntityCountry(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(index = true)
    val name: String,
)
