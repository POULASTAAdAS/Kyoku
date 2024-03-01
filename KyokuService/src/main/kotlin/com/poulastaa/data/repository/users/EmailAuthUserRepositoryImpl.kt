package com.poulastaa.data.repository.users

import com.poulastaa.data.model.utils.User
import com.poulastaa.data.model.utils.UserType
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.domain.dao.user.EmailAuthUser
import com.poulastaa.domain.repository.users.EmailAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toUser

class EmailAuthUserRepositoryImpl : EmailAuthUserRepository {
    private suspend fun findUser(email: String) = dbQuery {
        EmailAuthUser.find {
            EmailAuthUserTable.email eq email
        }.firstOrNull()
    }

    override suspend fun updateBDate(date: Long, email: String): SetBDateResponseStatus {
        val user = findUser(email) ?: return SetBDateResponseStatus.FAILURE

        return try {
            dbQuery {
                user.bDate = date
            }
            SetBDateResponseStatus.SUCCESS
        } catch (e: Exception) {
            SetBDateResponseStatus.FAILURE
        }
    }

    override suspend fun getCountryId(email: String): Int? = findUser(email)?.countryId

    override suspend fun getUser(email: String): User? = findUser(email)?.toUser(UserType.EMAIL_USER)
}