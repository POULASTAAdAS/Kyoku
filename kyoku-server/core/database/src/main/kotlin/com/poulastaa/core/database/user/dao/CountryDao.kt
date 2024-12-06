package com.poulastaa.core.database.user.dao

import com.poulastaa.core.database.user.entity.CountryEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CountryDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CountryDao>(CountryEntity)

    var country by CountryEntity.country
}