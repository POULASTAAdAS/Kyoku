package com.poulastaa.data.repository.users

import com.poulastaa.data.model.utils.User
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.repository.users.GoogleAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toUser

class GoogleAuthUserRepositoryImpl : GoogleAuthUserRepository {
    private suspend fun findUser(sub: String) = dbQuery {
        GoogleAuthUser.find {
            GoogleAuthUserTable.sub eq sub
        }.firstOrNull()
    }

    override suspend fun updateBDate(date: Long, sub: String): SetBDateResponseStatus {
        val user = findUser(sub) ?: return SetBDateResponseStatus.FAILURE

        return try {
            dbQuery {
                user.bDate = date
            }
            SetBDateResponseStatus.SUCCESS
        } catch (e: Exception) {
            SetBDateResponseStatus.FAILURE
        }
    }

    override suspend fun getCountryId(sub: String): Int? = findUser(sub)?.countryId

    override suspend fun getUser(sub: String): User? = findUser(sub)?.toUser(UserType.GOOGLE_USER)
}