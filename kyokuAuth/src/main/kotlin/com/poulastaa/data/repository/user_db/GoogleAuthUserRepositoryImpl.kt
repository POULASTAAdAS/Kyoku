package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.User
import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.db_table.GoogleAuthUserTable
import com.poulastaa.domain.dao.GoogleAuthUser
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.toGoogleAuthResponse

class GoogleAuthUserRepositoryImpl : GoogleAuthUserRepository {
    private suspend fun findUser(email: String): GoogleAuthUser? = dbQuery {
        GoogleAuthUser.find {
            GoogleAuthUserTable.email eq email
        }.firstOrNull()
    }

    override suspend fun createUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): GoogleAuthResponse {
        try {
            val user = findUser(email)

            if (user == null) {
                val newUser = dbQuery {
                    GoogleAuthUser.new {
                        this.userName = userName
                        this.sub = sub
                        this.email = email
                        this.profilePicUrl = pictureUrl
                    }
                }

                return newUser.toGoogleAuthResponse(status = UserCreationStatus.CREATED) // signup
            }

            return user.toGoogleAuthResponse(status = UserCreationStatus.CONFLICT) // login
        } catch (e: Exception) {
            return GoogleAuthResponse(status = UserCreationStatus.SOMETHING_WENT_WRONG)
        }
    }
}