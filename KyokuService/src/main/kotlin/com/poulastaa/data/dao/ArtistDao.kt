package com.poulastaa.data.dao

import com.poulastaa.domain.table.ArtistTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ArtistDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ArtistDao>(ArtistTable)

    val name by ArtistTable.name
    val profilePic by ArtistTable.profilePicUrl
    val points by ArtistTable.points
}