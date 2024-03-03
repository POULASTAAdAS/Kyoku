package com.poulastaa.data.model.db_table.user_listen_history

import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object GoogleUserListenHistoryTable : Table() {
    val userId = EmailUserListenHistoryTable.long("userId").references(GoogleAuthUserTable.id)
    val songId = EmailUserListenHistoryTable.long("songId").references(SongTable.id)
    val date = datetime("date").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(userId, songId, date)
}