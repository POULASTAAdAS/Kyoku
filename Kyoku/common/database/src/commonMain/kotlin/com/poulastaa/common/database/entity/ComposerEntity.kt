package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "composer")
data class ComposerEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val coverImage: String?,
    val followers: Long,
)
