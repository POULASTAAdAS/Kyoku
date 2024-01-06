package com.example.data.repository.user

import com.example.data.model.UserCreationResponse
import com.example.domain.model.GoogleAuthUser
import com.example.domain.repository.user.GoogleAuthUserRepository
import com.example.plugins.dbQuery
import com.example.routes.auth.common.UserCreationStatus
import org.jetbrains.exposed.exceptions.ExposedSQLException

class GoogleAuthUserRepositoryImpl : GoogleAuthUserRepository {
    override suspend fun createUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): UserCreationResponse {
        return try {
            dbQuery {
                GoogleAuthUser.new {
                    this.userName = userName
                    this.sub = sub
                    this.email = email
                    this.pictureUrl = pictureUrl
                }
            }
            UserCreationResponse(
                status = UserCreationStatus.CREATED
            )
        } catch (e: ExposedSQLException) {
            UserCreationResponse( // todo query other values
                status = UserCreationStatus.CONFLICT
            )
        } catch (e: Exception) {
            UserCreationResponse(
                status = UserCreationStatus.SOMETHING_WENT_WRONG
            )
        }
    }
}