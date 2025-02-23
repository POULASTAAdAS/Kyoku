package com.poulastaa.core.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.poulastaa.core.database.entity.EntityPrevArtist
import com.poulastaa.core.database.entity.EntityPrevSong
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist

data class SuggestedArtistWithSong(
    @Embedded
    val artist: EntityPrevArtist,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = EntityRelationSuggestedSongByArtist::class,
            parentColumn = "artistId",
            entityColumn = "songId"
        )
    )
    val list: List<EntityPrevSong>,
)
