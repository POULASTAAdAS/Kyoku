package com.poulastaa.core.database.entity.app

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntitySongCountry : Table(name = "SongCountry") {
    val songId = long("songId").references(EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val countryId = integer("countryId").references(EntityCountry.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, countryId)
}