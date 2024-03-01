package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.LongIdTable

object CountryGenreRelationTable : LongIdTable() {
    val countryId = integer("countryId").references(CountryTable.id)
    val genreId = integer("genreId").references(GenreTable.id)
    val points = long("points").default(0)
}