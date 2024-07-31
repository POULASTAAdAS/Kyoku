package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.CountryTable
import com.poulastaa.domain.table.SongTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object SongCountryRelationTable : Table() {
    val songId = long("songId").references(SongTable.id, onDelete = ReferenceOption.CASCADE)
    val countryId = integer("countryId").references(CountryTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, countryId)
}