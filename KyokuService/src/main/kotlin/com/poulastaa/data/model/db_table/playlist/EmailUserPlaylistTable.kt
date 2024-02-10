package com.poulastaa.data.model.db_table.playlist

import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import org.jetbrains.exposed.dao.id.UUIDTable

object EmailUserPlaylistTable : UUIDTable() {
    val songId = long("songId").references(SongTable.id)
    val userEmail = varchar("emailId", 320).references(EmailAuthUserTable.email)
}