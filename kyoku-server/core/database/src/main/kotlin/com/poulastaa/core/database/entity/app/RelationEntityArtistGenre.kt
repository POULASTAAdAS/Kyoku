package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityArtistGenre : Table(name = "ArtistGenre") {
    val artistId = long("artistId").references(EntityArtist.id, onDelete = ReferenceOption.CASCADE)
    val genreId = integer("genreId").references(EntityGenre.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(artistId, genreId)
}