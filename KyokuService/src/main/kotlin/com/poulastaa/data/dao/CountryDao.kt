package com.poulastaa.data.dao

import com.poulastaa.domain.table.other.CountryTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CountryDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CountryDao>(CountryTable)

    val name by CountryTable.name
}