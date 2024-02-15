package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.domain.dao.user.GoogleAuthUser
import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.plugins.dbQuery

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
}