package com.poulastaa.core.database.entity

import androidx.room.Entity
import com.poulastaa.core.domain.model.DayType

@Entity(
    primaryKeys = ["id", "dayType"]
)
data class DayTypeSongPrevEntity(
    val id: Long,
    val dayType: DayType,
    val coverImage: String,
)