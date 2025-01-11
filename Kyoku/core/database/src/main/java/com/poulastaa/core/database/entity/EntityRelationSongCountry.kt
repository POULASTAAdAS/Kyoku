package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "countryId",
        "songId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntitySong::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntityCountry::class,
            parentColumns = ["id"],
            childColumns = ["countryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityRelationSongCountry(
    @ColumnInfo(index = true)
    val songId: Long,
    val countryId: Int,
)
