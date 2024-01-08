package com.example.data.repository.user_db

import com.example.data.model.auth.GoogleSignInResponse
import com.example.data.model.auth.UserCreationStatus
import com.example.data.model.database.GoogleAuthUserTable
import com.example.domain.dao.GoogleAuthUser
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import com.example.plugins.dbQuery
import org.jetbrains.exposed.exceptions.ExposedSQLException

class GoogleAuthUserRepositoryImpl : GoogleAuthUserRepository {
    override suspend fun createUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): GoogleSignInResponse {
        return try {
            dbQuery {
                GoogleAuthUser.new {
                    this.userName = userName
                    this.sub = sub
                    this.email = email
                    this.profilePicUrl = pictureUrl
                }
            }
            GoogleSignInResponse(
                status = UserCreationStatus.CREATED,
                userName = userName,
                profilePic = pictureUrl,
                token = ""
            )
        } catch (e: ExposedSQLException) {
            val user = GoogleAuthUser.find {
                GoogleAuthUserTable.sub eq sub
            }.first()

            GoogleSignInResponse( // todo query other values
                status = UserCreationStatus.CONFLICT,
                userName = user.userName,
                profilePic = user.profilePicUrl,
                token = "",
                data = emptyList()
            )
        } catch (e: Exception) {
            GoogleSignInResponse(
                status = UserCreationStatus.SOMETHING_WENT_WRONG
            )
        }
    }
}