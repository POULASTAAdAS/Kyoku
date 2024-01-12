package com.example.domain.dao

import com.example.data.model.database_table.EmailAuthUserTable
import com.example.data.model.database_table.InvalidRefreshTokenTable
import com.example.data.model.database_table.InvalidRefreshTokenTable.references
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class InvalidRefreshToken(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<InvalidRefreshToken>(InvalidRefreshTokenTable)

    var createTime by InvalidRefreshTokenTable.createTime
    var token by InvalidRefreshTokenTable.oldRefreshToken
    var emailUserId by InvalidRefreshTokenTable.emailUserId references EmailAuthUserTable.id
}