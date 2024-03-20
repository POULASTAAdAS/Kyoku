package com.poulastaa.data.model.db_table.user_album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PasskeyUserAlbumRelation : Table() {
    val userId = long("userId").references(PasskeyAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId").references(AlbumTable.id)
    val points  = integer("points").default(0)

    override val primaryKey = PrimaryKey(userId, albumId)
}