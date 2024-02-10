package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Embedded
import androidx.room.Relation

data class PlaylistRelationTable(
    @Embedded val playlist: PlaylistTable = PlaylistTable(),
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    ) val songs: List<SongTable> = emptyList()
)
