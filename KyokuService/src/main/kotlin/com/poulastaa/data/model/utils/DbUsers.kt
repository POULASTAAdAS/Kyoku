package com.poulastaa.data.model.utils

import com.poulastaa.domain.repository.users.EmailAuthUserRepository
import com.poulastaa.domain.repository.users.GoogleAuthUserRepository
import com.poulastaa.domain.repository.users.PasskeyAuthUserRepository

class DbUsers(
    private val emailUser: EmailAuthUserRepository,
    private val googleUser: GoogleAuthUserRepository,
    private val passekyUser: PasskeyAuthUserRepository
) {
    suspend fun getDbUser(userTypeHelper: UserTypeHelper) = when (userTypeHelper.userType) {
        UserType.GOOGLE_USER -> googleUser.getUser(userTypeHelper.headerId)

        UserType.EMAIL_USER -> emailUser.getUser(userTypeHelper.headerId)

        UserType.PASSKEY_USER -> passekyUser.getUser(userTypeHelper.headerId)
    }

    suspend fun storeBDate(userTypeHelper: UserTypeHelper, date: Long) = when (userTypeHelper.userType) {
        UserType.GOOGLE_USER -> googleUser.updateBDate(date, userTypeHelper.headerId)

        UserType.EMAIL_USER -> emailUser.updateBDate(date, userTypeHelper.headerId)

        UserType.PASSKEY_USER -> passekyUser.updateBDate(date, userTypeHelper.headerId)
    }

    suspend fun getCountryId(helper: UserTypeHelper) = when (helper.userType) {
        UserType.GOOGLE_USER -> googleUser.getCountryId(helper.headerId)

        UserType.EMAIL_USER -> emailUser.getCountryId(helper.headerId)

        UserType.PASSKEY_USER -> passekyUser.getCountryId(helper.headerId)
    }
}
