package com.poulastaa.core.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntitySong

data class PlaylistWithSong(
    @Embedded
    val playlist: EntityPlaylist,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EntityRelationSongPlaylist::class,
            parentColumn = "playlistId",
            entityColumn = "songId"
        )
    )
    val list: List<EntitySong>,
)
