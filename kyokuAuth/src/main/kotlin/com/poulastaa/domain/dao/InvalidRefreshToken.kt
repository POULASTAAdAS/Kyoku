package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.EmailAuthUserTable
import com.poulastaa.data.model.db_table.EmailAuthUserTable.references
import com.poulastaa.data.model.db_table.InvalidRefreshTokenTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InvalidRefreshToken(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<InvalidRefreshToken>(InvalidRefreshTokenTable)

    var createTime by InvalidRefreshTokenTable.createTime
    var token by InvalidRefreshTokenTable.oldRefreshToken
    var emailUserId by InvalidRefreshTokenTable.emailUserId references EmailAuthUserTable.id
}