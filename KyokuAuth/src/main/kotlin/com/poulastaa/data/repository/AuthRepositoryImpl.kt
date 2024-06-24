package com.poulastaa.data.repository

import com.poulastaa.data.dao.user.EmailAuthUser
import com.poulastaa.data.dao.user.GoogleAuthUser
import com.poulastaa.data.model.GoogleAuthResPayload
import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.res.Payload
import com.poulastaa.data.model.auth.response.EmailAuthRes
import com.poulastaa.data.model.auth.response.GoogleAuthRes
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.domain.repository.AuthRepository
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.domain.table.user.GoogleAuthUserTable
import com.poulastaa.plugins.dbQuery

class AuthRepositoryImpl : AuthRepository {
    override suspend fun createEmailUser(
        userName: String,
        email: String,
        password: String,
        refreshToken: String,
        countryId: Int,
    ): UserAuthStatus {
        val user = findEmailUser(email)

        return if (user == null) {
            dbQuery {
                EmailAuthUser.new {
                    this.userName = userName
                    this.email = email
                    this.password = password
                    this.refreshToken = refreshToken
                    this.countryId = countryId
                }
            }.let {
                UserAuthStatus.CREATED
            }
        } else {
            UserAuthStatus.CONFLICT
        }
    }

    override suspend fun loginEmailUser(email: String, password: String, refreshToken: String): EmailAuthRes {
        val user = findEmailUser(email) ?: return EmailAuthRes(
            status = UserAuthStatus.USER_NOT_FOUND
        )

        if (user.password != password) return EmailAuthRes(
            status = UserAuthStatus.PASSWORD_DOES_NOT_MATCH
        )

        if (!user.emailVerified) return EmailAuthRes(
            status = UserAuthStatus.EMAIL_NOT_VERIFIED
        )

        if (user.bDate == null) return EmailAuthRes(
            status = UserAuthStatus.USER_FOUND_STORE_B_DATE
        )

        // todo check for artist and genre entry

        return EmailAuthRes(
            status = UserAuthStatus.USER_FOUND_HOME,
            user = User(
                email = user.email,
                userName = user.userName,
                profilePic = user.profilePic
            )
        )
    }

    override suspend fun googleAuth(
        payload: Payload,
        countryId: Int,
    ): GoogleAuthResPayload {
        val user = findGoogleUser(payload.email)

        if (user == null) {
            val newUser = dbQuery {
                GoogleAuthUser.new {
                    this.userName = payload.userName
                    this.sub = payload.sub
                    this.email = payload.email
                    this.profilePicUrl = payload.pictureUrl
                    this.countryId = countryId
                }
            }

            return GoogleAuthResPayload(
                response = GoogleAuthRes(
                    status = UserAuthStatus.CREATED,
                    user = User(
                        email = newUser.email,
                        userName = newUser.userName,
                        profilePic = newUser.profilePicUrl
                    )
                ),
                userId = newUser.id.value
            )
        }

        if (user.bDate == null) return GoogleAuthResPayload(
            response = GoogleAuthRes(
                status = UserAuthStatus.USER_FOUND_STORE_B_DATE,
                user = User(
                    email = user.email,
                    userName = user.userName,
                    profilePic = user.profilePicUrl
                )
            ),
            userId = user.id.value
        )

        // todo check for artist and genre entry

        return GoogleAuthResPayload(
            response = GoogleAuthRes(
                status = UserAuthStatus.USER_FOUND_HOME,
                user = User(
                    email = user.email,
                    userName = user.userName,
                    profilePic = user.profilePicUrl
                )
            ),
            userId = user.id.value
        )
    }


    private suspend fun findEmailUser(email: String) = dbQuery {
        EmailAuthUser.find {
            EmailAuthUserTable.email eq email
        }.singleOrNull()
    }

    private suspend fun findGoogleUser(email: String) = dbQuery {
        GoogleAuthUser.find {
            GoogleAuthUserTable.email eq email
        }.singleOrNull()
    }
}