package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.IntIdTable

object ArtistTable : IntIdTable() {
    val name = varchar("name", 120).uniqueIndex()
    val profilePicUrl = text("profilePicUrl")
    val country = integer("country").references(CountryTable.id)
    val genre = integer("genre").references(GenreTable.id)
    val points = long("points").default(0)
}