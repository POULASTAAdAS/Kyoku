package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.data.model.db_table.PasskeyAuthUserTable
import com.poulastaa.domain.dao.PasskeyAuthUser
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toPasskeyAuthResponse

class PasskeyAuthUserRepositoryImpl : PasskeyAuthUserRepository {
    override suspend fun findUserByEmail(email: String): PasskeyAuthUser? = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.email eq email
        }.firstOrNull()
    }

    override suspend fun createUser(
        userId: String,
        email: String,
        userName: String,
    ): PasskeyAuthResponse {
        val user = findUserByUserId(userId)

        if (user == null) {
            val newUser = dbQuery {
                PasskeyAuthUser.new {
                    this.userId = userId
                    this.email = email
                    this.displayName = displayName
                }
            }

            return newUser.toPasskeyAuthResponse(status = UserCreationStatus.CREATED)
        }

        return user.toPasskeyAuthResponse(status = UserCreationStatus.CONFLICT)
    }

    override suspend fun getUser(userId: String): PasskeyAuthUser? = findUserByUserId(userId)


    private suspend fun findUserByUserId(userId: String) = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.userId eq userId
        }.firstOrNull()
    }
}