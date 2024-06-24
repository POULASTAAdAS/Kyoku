package com.poulastaa.data.dao.other

import com.poulastaa.domain.table.other.CountryTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Country(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Country>(CountryTable)

    val name by CountryTable.name
}