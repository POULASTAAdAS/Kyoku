package com.poulastaa.common.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "song_composer",
    primaryKeys = ["songId", "composerId"],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ComposerEntity::class,
            parentColumns = ["id"],
            childColumns = ["composerId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("songId"), Index("composerId")],
)
data class SongComposerEntity(
    val songId: Long,
    val composerId: Long,
)
