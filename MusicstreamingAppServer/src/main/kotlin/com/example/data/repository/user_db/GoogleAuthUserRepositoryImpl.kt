package com.example.data.repository.user_db

import com.example.data.model.auth.res.GoogleSignInResponse
import com.example.data.model.auth.stat.UserCreationStatus
import com.example.data.model.database_table.GoogleAuthUserTable
import com.example.domain.dao.GoogleAuthUser
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import com.example.plugins.dbQuery
import com.example.util.toGoogleSignInResponse

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
    ): GoogleSignInResponse {
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

                return newUser.toGoogleSignInResponse(status = UserCreationStatus.CREATED) // signup
            }


            return user.toGoogleSignInResponse(status = UserCreationStatus.CONFLICT) // login
        } catch (e: Exception) {
            return GoogleSignInResponse(
                status = UserCreationStatus.SOMETHING_WENT_WRONG
            )
        }
    }
}