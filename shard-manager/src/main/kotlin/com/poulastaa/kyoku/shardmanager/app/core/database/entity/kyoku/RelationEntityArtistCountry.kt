package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityArtistCountry : Table(name = "ArtistCountry") {
    val artistId = long("artistId").references(EntityArtist.id, onDelete = ReferenceOption.CASCADE)
    val countryId = integer("countryId").references(EntityCountry.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(artistId, countryId)
}