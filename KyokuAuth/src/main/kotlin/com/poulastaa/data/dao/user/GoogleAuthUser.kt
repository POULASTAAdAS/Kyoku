package com.poulastaa.data.dao.user

import com.poulastaa.domain.table.user.GoogleAuthUserTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class GoogleAuthUser(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<GoogleAuthUser>(GoogleAuthUserTable)

    var userName by GoogleAuthUserTable.userName
    var email by GoogleAuthUserTable.email
    var sub by GoogleAuthUserTable.sub
    var profilePicUrl by GoogleAuthUserTable.profilePic
    var bDate by GoogleAuthUserTable.bDate
    var countryId by GoogleAuthUserTable.countryId
}