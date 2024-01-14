package com.example.domain.dao

import com.example.data.model.database_table.SessionStorageTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SessionStorageDB(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SessionStorageDB>(SessionStorageTable)

    var sessionId by SessionStorageTable.sessionId
    var value by SessionStorageTable.value
}