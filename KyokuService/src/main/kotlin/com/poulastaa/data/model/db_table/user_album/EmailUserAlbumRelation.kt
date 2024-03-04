package com.poulastaa.data.model.db_table.user_album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import org.jetbrains.exposed.sql.Table

object EmailUserAlbumRelation : Table() {
    val userId = long("userId").references(EmailAuthUserTable.id)
    val albumId = long("albumId").references(AlbumTable.id)
    val points = integer("points").default(0)

    override val primaryKey = PrimaryKey(userId, albumId)
}