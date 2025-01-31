package com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntityCountry
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoCountry(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DaoCountry>(EntityCountry)

    var country by EntityCountry.country
}