package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.CountryGenreRelationTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CountryGenreRelation(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<CountryGenreRelation>(CountryGenreRelationTable)

    val countryId by CountryGenreRelationTable.countryId
    val genreId by CountryGenreRelationTable.genreId
}