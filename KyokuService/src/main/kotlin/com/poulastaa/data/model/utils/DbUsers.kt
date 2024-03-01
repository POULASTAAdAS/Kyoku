package com.poulastaa.data.model.utils

import com.poulastaa.domain.repository.users.EmailAuthUserRepository
import com.poulastaa.domain.repository.users.GoogleAuthUserRepository
import com.poulastaa.domain.repository.users.PasskeyAuthUserRepository

class DbUsers(
    private val emailUser: EmailAuthUserRepository,
    private val googleUser: GoogleAuthUserRepository,
    private val passekyUser: PasskeyAuthUserRepository
) {
    suspend fun gerDbUser(userTypeHelper: UserTypeHelper) = when (userTypeHelper.userType) {
        UserType.GOOGLE_USER -> googleUser.getUser(userTypeHelper.id)

        UserType.EMAIL_USER -> emailUser.getUser(userTypeHelper.id)

        UserType.PASSKEY_USER -> passekyUser.getUser(userTypeHelper.id)
    }

    suspend fun storeBDate(userTypeHelper: UserTypeHelper, date: Long) = when (userTypeHelper.userType) {
        UserType.GOOGLE_USER -> googleUser.updateBDate(date, userTypeHelper.id)

        UserType.EMAIL_USER -> emailUser.updateBDate(date, userTypeHelper.id)

        UserType.PASSKEY_USER -> passekyUser.updateBDate(date, userTypeHelper.id)
    }

    suspend fun getCountryId(helper: UserTypeHelper) = when (helper.userType) {
        UserType.GOOGLE_USER -> googleUser.getCountryId(helper.id)

        UserType.EMAIL_USER -> emailUser.getCountryId(helper.id)

        UserType.PASSKEY_USER -> passekyUser.getCountryId(helper.id)
    }
}
