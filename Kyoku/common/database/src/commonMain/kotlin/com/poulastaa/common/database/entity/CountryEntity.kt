package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class CountryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val code: String,
)
