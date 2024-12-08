package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.CountryEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CountryDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CountryDao>(CountryEntity)

    var country by CountryEntity.country
}