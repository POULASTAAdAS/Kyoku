package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.sql.Table

object SongGenreRelationTable : Table() {
    val songId = long("songId").references(SongTable.id)
    val genreId = integer("genreId").references(GenreTable.id)

    override val primaryKey = PrimaryKey(songId, genreId)
}