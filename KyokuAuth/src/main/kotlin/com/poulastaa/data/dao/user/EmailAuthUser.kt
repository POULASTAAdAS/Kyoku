package com.poulastaa.data.dao.user

import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.utils.Constants.PROFILE_PIC_ROOT_DIR
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EmailAuthUser(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EmailAuthUser>(EmailAuthUserTable)

    var userName by EmailAuthUserTable.userName
    var email by EmailAuthUserTable.email
    var password by EmailAuthUserTable.password
    var emailVerified by EmailAuthUserTable.emailVerified
    var emailVerificationDone by EmailAuthUserTable.emailVerificationDone
    private var profilePic by EmailAuthUserTable.profilePic
    var refreshToken by EmailAuthUserTable.refreshToken
    var bDate by EmailAuthUserTable.bDate
    var countryId by EmailAuthUserTable.countryId

    fun constructProfilePic() = System.getenv("SERVICE_URL") +
            this.profilePic.replace(PROFILE_PIC_ROOT_DIR, "")

    fun updateProfilePic(profilePic: String) {
        this.profilePic = profilePic
    }
}