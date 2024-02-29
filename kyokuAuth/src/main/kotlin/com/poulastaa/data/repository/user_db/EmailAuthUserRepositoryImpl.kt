package com.poulastaa.data.repository.user_db

import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.jwt.*
import com.poulastaa.data.model.db_table.EmailAuthUserTable
import com.poulastaa.data.model.db_table.InvalidRefreshTokenTable
import com.poulastaa.domain.dao.EmailAuthUser
import com.poulastaa.domain.dao.InvalidRefreshToken
import com.poulastaa.domain.repository.user_db.EmailAuthUserRepository
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.Constants.REFRESH_TOKEN_DEFAULT_TIME
import com.poulastaa.utils.constructProfileUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class EmailAuthUserRepositoryImpl : EmailAuthUserRepository {

    override suspend fun createUser(
        userName: String,
        email: String,
        password: String,
        refreshToken: String,
        countryId: Int
    ): UserCreationStatus = try {
        val user = findUser(email)

        if (user == null) {
            dbQuery {
                EmailAuthUser.new {
                    this.userName = userName
                    this.email = email
                    this.password = password
                    this.refreshToken = refreshToken
                    this.countryId = countryId
                }
            }.let {
                UserCreationStatus.CREATED
            }
        } else {
            UserCreationStatus.CONFLICT
        }
    } catch (e: Exception) {
        UserCreationStatus.SOMETHING_WENT_WRONG
    }

    override suspend fun updateVerificationStatus(
        email: String
    ): UpdateEmailVerificationStatus {
        return try {
            val user = findUser(email) ?: return UpdateEmailVerificationStatus.USER_NOT_FOUND

            if (!user.emailVerified) {
                dbQuery {
                    user.emailVerified = true // update query
                }

                UpdateEmailVerificationStatus.VERIFIED
            } else UpdateEmailVerificationStatus.VERIFIED
        } catch (e: Exception) {
            UpdateEmailVerificationStatus.SOMETHING_WENT_WRONG
        }
    }

    // this will be hit repeated time while signing up
    override suspend fun checkEmailVerification(email: String): EmailVerificationStatus {
        return try {
            val user = findUser(email) ?: return EmailVerificationStatus.USER_NOT_FOUND

            return if (user.emailVerified) EmailVerificationStatus.VERIFIED
            else EmailVerificationStatus.UN_VERIFIED
        } catch (e: Exception) {
            EmailVerificationStatus.SOMETHING_WENT_WRONG
        }
    }


    override suspend fun loginUser(
        email: String,
        password: String,
        accessToken: String,
        refreshToken: String
    ): EmailLoginResponse {
        try {
            val user = findUser(email)
                ?: return EmailLoginResponse(
                    status = EmailLoginStatus.USER_DOES_NOT_EXISTS
                )

            if (user.password != password) {
                return EmailLoginResponse(
                    status = EmailLoginStatus.PASSWORD_DOES_NOT_MATCH
                )
            }

            if (!user.emailVerified) {
                return EmailLoginResponse(
                    status = EmailLoginStatus.EMAIL_NOT_VERIFIED
                )
            }

            return EmailLoginResponse(
                status = EmailLoginStatus.USER_PASS_MATCHED,
                accessToken = accessToken,
                refreshToken = refreshToken,
                user = User(
                    userName = user.userName,
                    profilePic = constructProfileUrl()
                ),
                data = emptyList()
            )
        } catch (e: Exception) {
            return EmailLoginResponse(
                status = EmailLoginStatus.SOMETHING_WENT_WRONG
            )
        }
    }

    override suspend fun checkIfUSerExistsToSendForgotPasswordMail(email: String): SendForgotPasswordMailStatus {
        return try {
            findUser(email) ?: return SendForgotPasswordMailStatus.USER_NOT_FOUND
            SendForgotPasswordMailStatus.USER_EXISTS
        } catch (e: Exception) {
            SendForgotPasswordMailStatus.SOMETHING_WENT_WRONG
        }
    }

    override suspend fun resetPassword(email: String, password: String): PasswordResetStatus {
        return try {
            val user = findUser(email) ?: return PasswordResetStatus.USER_NOT_FOUND

            if (user.password == password) return PasswordResetStatus.SAME_AS_OLD_PASSWORD

            dbQuery {
                user.password = password // update query
            }

            PasswordResetStatus.SUCCESSFUL
        } catch (e: Exception) {
            PasswordResetStatus.SOMETHING_WENT_WRONG
        }
    }

    override suspend fun getUserProfilePic(email: String): File? {
        try {
            val user = findUser(email) ?: return null

            return File(user.profilePic)
        } catch (e: IOException) {
            return null
        }
    }

    override suspend fun updateRefreshToken(
        email: String,
        oldRefreshToken: String,
        refreshToken: String
    ): RefreshTokenUpdateStatus {
        return try {
            val user = findUser(email) ?: return RefreshTokenUpdateStatus.USER_NOT_FOUND

            val isUsedToken = checkIfDuplicateToken(
                token = oldRefreshToken,
                userId = user.id.value
            )

            if (isUsedToken) return RefreshTokenUpdateStatus.DUPLICATE_TOKEN

            dbQuery {
                user.refreshToken = refreshToken

                // add token
                InvalidRefreshToken.new {
                    this.token = oldRefreshToken
                    this.createTime = System.currentTimeMillis() + REFRESH_TOKEN_DEFAULT_TIME
                    this.userId = user.id.value
                }
            }

            RefreshTokenUpdateStatus.UPDATED
        } catch (e: Exception) {
            RefreshTokenUpdateStatus.SOMETHING_WENT_WRONG
        }
    }

    private suspend fun findUser(email: String) = dbQuery {
        EmailAuthUser.find {
            EmailAuthUserTable.email eq email
        }.firstOrNull()
    }

    private suspend fun checkIfDuplicateToken(token: String, userId: Long): Boolean {
        val entries = dbQuery {
            InvalidRefreshToken.find {
                InvalidRefreshTokenTable.userId eq userId
            }.toList()
        }

        var status = false

        entries.forEach {
            if (it.token == token) {
                status = true
                return@forEach
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            dbQuery {
                entries.forEach {
                    val isAlive = it.createTime > System.currentTimeMillis()

                    if (!isAlive) it.delete()
                }
            }
        }

        return status
    }

    override suspend fun checkVerificationMailStatus(email: String): ResendVerificationMailStatus {
        val response = dbQuery {
            EmailAuthUser.find {
                EmailAuthUserTable.email eq email
            }.firstOrNull()?.emailVerified
        } ?: return ResendVerificationMailStatus.SOMETHING_WENT_WRONG

        return if (response) ResendVerificationMailStatus.EMAIL_ALREADY_VERIFIED
        else ResendVerificationMailStatus.VERIFICATION_MAIL_SEND
    }
}