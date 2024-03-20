package com.poulastaa.data.model.db_table.user_fev

import com.poulastaa.data.model.db_table.song.SongTable
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object EmailUserFavouriteTable : Table() {
    val userId = long("userId").references(EmailAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
    val songId = long("songId").references(SongTable.id, onDelete = ReferenceOption.CASCADE)
    val date = datetime("date").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(userId, songId)
}