package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.AlbumTable
import com.poulastaa.domain.table.ArtistTable
import com.poulastaa.domain.table.CountryTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object ArtistCountryRelationTable : Table() {
    val artistId = long("artistId").references(ArtistTable.id, onDelete = ReferenceOption.CASCADE)
    val countryId = integer("countryId").references(CountryTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(artistId, countryId)
}