package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.EntityCountry
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoCountry(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DaoCountry>(EntityCountry)

    var country by EntityCountry.country
}