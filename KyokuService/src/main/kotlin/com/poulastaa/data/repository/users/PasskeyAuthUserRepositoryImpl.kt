package com.poulastaa.data.repository.users

import com.poulastaa.data.model.User
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.domain.repository.users.PasskeyAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toUser

class PasskeyAuthUserRepositoryImpl : PasskeyAuthUserRepository {
    private suspend fun findUser(userId: String) = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.userId eq userId
        }.firstOrNull()
    }

    override suspend fun updateBDate(date: Long, userId: String): SetBDateResponseStatus {
        val user = findUser(userId) ?: return SetBDateResponseStatus.FAILURE

        return try {
            dbQuery {
                user.bDate = date
            }
            SetBDateResponseStatus.SUCCESS
        } catch (e: Exception) {
            SetBDateResponseStatus.FAILURE
        }
    }

    override suspend fun getCountryId(userId: String): Int? = findUser(userId)?.countryId

    override suspend fun getUser(userId: String): User? = findUser(userId)?.toUser(UserType.PASSKEY_USER)
}