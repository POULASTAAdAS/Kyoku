package com.poulastaa.data.dao.session

import com.poulastaa.domain.table.session.SessionStorageTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SessionStorageDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SessionStorageDao>(SessionStorageTable)

    var sessionId by SessionStorageTable.sessionId
    var value by SessionStorageTable.value
}