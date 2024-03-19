package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import com.poulastaa.domain.dao.user.PasskeyAuthUser
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toPasskeyAuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
        profilePic: String,
        countryId: Int
    ): PasskeyAuthResponse {
        val user = findUserByEmail(email)

        if (user == null) {
            val newUser = dbQuery {
                PasskeyAuthUser.new {
                    this.userId = userId
                    this.email = email
                    this.displayName = userName
                    this.profilePic = profilePic
                    this.countryId = countryId
                }
            }

            return newUser.toPasskeyAuthResponse(status = UserCreationStatus.CREATED)
        }

        return PasskeyAuthResponse(
            status = UserCreationStatus.CONFLICT
        )
    }

    override suspend fun loginUser(userId: String): Pair<String, PasskeyAuthResponse> {
        val user = findUserByUserId(userId) ?: return Pair(
            first = "",
            second = PasskeyAuthResponse(
                status = UserCreationStatus.USER_NOT_FOUND
            )
        )

        return withContext(Dispatchers.IO) {
            // todo get all data

            Pair(
                first = user.email,
                second = user.toPasskeyAuthResponse(status = UserCreationStatus.CONFLICT)
            )
        }
    }


    private suspend fun findUserByUserId(userId: String) = dbQuery {
        PasskeyAuthUser.find {
            PasskeyAuthUserTable.userId eq userId
        }.firstOrNull()
    }
}