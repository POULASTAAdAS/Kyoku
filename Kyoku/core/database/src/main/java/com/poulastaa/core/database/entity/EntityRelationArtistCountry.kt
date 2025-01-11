package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "countryId",
        "artistId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntityArtist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
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
data class EntityRelationArtistCountry(
    @ColumnInfo(index = true)
    val artistId: Long,
    val countryId: Int,
)
