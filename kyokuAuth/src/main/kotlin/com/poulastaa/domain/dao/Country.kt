package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.CountryTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Country(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Country>(CountryTable)

    val name by CountryTable.name
}

