package com.poulastaa.data.model.db_table.playlist

import org.jetbrains.exposed.dao.id.LongIdTable

object PlaylistTable : LongIdTable() {
    val name = text("name")
}