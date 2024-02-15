package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import com.poulastaa.plugins.dbQuery

class PasskeyAuthUserRepositoryImpl: PasskeyAuthUserRepository {
    private suspend fun findUser(email: String) = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.email eq email
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
}