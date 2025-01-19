package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.EntityGenre
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoGenre(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DaoGenre>(EntityGenre)

    val genre by EntityGenre.genre
    val cover by EntityGenre.cover
    var popularity by EntityGenre.popularity
}