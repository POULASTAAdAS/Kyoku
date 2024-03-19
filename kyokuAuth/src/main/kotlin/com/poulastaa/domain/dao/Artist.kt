package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.ArtistTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Artist(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Artist>(ArtistTable)

    val name by ArtistTable.name
    val profilePicUrl by ArtistTable.profilePicUrl
    val country by ArtistTable.country
    val genre by ArtistTable.genre
    var points by ArtistTable.points
}