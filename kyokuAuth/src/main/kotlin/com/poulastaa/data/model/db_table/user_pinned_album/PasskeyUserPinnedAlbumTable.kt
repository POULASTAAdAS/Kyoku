package com.poulastaa.data.model.db_table.user_pinned_album

import com.poulastaa.data.model.db_table.AlbumTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PasskeyUserPinnedAlbumTable : Table() {
    val userId = long("userId").references(PasskeyAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
    val albumId = long("albumId").references(AlbumTable.id)

    override val primaryKey = PrimaryKey(userId, albumId)
}