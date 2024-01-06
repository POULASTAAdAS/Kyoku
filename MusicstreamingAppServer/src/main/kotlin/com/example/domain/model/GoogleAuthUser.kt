package com.example.domain.model

import com.example.data.model.database.GoogleAuthUserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GoogleAuthUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GoogleAuthUser>(GoogleAuthUserTable)

    var userName by GoogleAuthUserTable.userName
    var email by GoogleAuthUserTable.email
    var sub by GoogleAuthUserTable.sub
    var pictureUrl by GoogleAuthUserTable.pictureUrl
}