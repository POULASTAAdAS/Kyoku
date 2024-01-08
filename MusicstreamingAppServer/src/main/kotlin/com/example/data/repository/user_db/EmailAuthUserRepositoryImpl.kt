package com.example.data.repository.user_db

import com.example.data.model.auth.*
import com.example.data.model.database.EmailAuthUserTable
import com.example.domain.dao.EmailAuthUser
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.plugins.dbQuery
import com.example.util.constructProfileUrl
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.update
import java.io.File

class EmailAuthUserRepositoryImpl : EmailAuthUserRepository {
    override suspend fun createUser(
        userName: String,
        email: String,
        password: String,
        token: String
    ): EmailSignInResponse = try {
        dbQuery {
            EmailAuthUser.new {
                this.userName = userName
                this.email = email
                this.password = password
            }
        }.let {
            EmailSignInResponse(
                userName = it.userName,
                token = token,
                status = UserCreationStatus.CREATED,
                profilePic = constructProfileUrl()
            )
        }
    } catch (e: ExposedSQLException) {
        EmailSignInResponse(
            status = UserCreationStatus.CONFLICT
        )
    } catch (e: Exception) {
        EmailSignInResponse(
            status = UserCreationStatus.SOMETHING_WENT_WRONG
        )
    }

    override suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus =
        try {
            if (
                !dbQuery {
                    EmailAuthUser.find {
                        EmailAuthUserTable.email eq email
                    }.first().emailVerified
                }
            ) {
                dbQuery {
                    EmailAuthUserTable.update {
                        this.email eq email
                        it[this.emailVerified] = true
                    }
                }

                UpdateEmailVerificationStatus.DONE
            } else
                UpdateEmailVerificationStatus.ALREADY_VERIFIED
        } catch (e: Exception) {
            println("error updating verification status: " + e.message)  // todo delete user
            UpdateEmailVerificationStatus.SOMETHING_WENT_WRONG
        }

    // this will be hit repeated time while signing up
    override suspend fun checkEmailVerification(email: String): EmailVerificationResponse =
        try {
            dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first().emailVerified
            }.let {
                if (it) EmailVerificationResponse(
                    status = EmailVerificationStatus.VERIFIED
                )
                else EmailVerificationResponse(
                    status = EmailVerificationStatus.UN_VERIFIED
                )
            }
        } catch (e: Exception) {
            EmailVerificationResponse(
                status = EmailVerificationStatus.SOMETHING_WENT_WRONG
            )
        }


    override suspend fun loginUser(
        email: String,
        password: String,
        token: String
    ): EmailLoginResponse {
        return try {
            val user = dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first()
            }

            if (user.password == password) {
                if (user.emailVerified)
                    EmailLoginResponse(
                        userName = user.userName,
                        status = EmailLoginStatus.USER_PASS_MATCHED,
                        token = token,
                        profilePic = constructProfileUrl(),
                        data = emptyList() // todo add data
                    )
                else
                    EmailLoginResponse(
                        status = EmailLoginStatus.EMAIL_NOT_VERIFIED
                    )
            } else
                EmailLoginResponse(
                    status = EmailLoginStatus.PASSWORD_DOES_NOT_MATCH
                )
        } catch (e: NoSuchElementException) {
            EmailLoginResponse(
                status = EmailLoginStatus.USER_DOES_NOT_EXISTS
            )
        } catch (e: Exception) {
            EmailLoginResponse(
                status = EmailLoginStatus.SOMETHING_WENT_WRONG
            )
        }
    }

    override suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail {
        return try {
            dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }
            }

            SendForgotPasswordMail(
                status = SendVerificationMailStatus.USER_EXISTS
            )
        } catch (e: NoSuchElementException) {
            SendForgotPasswordMail(
                status = SendVerificationMailStatus.USER_NOT_FOUND
            )
        } catch (e: Exception) {
            SendForgotPasswordMail(
                status = SendVerificationMailStatus.SOMETHING_WENT_WRONG
            )
        }
    }

    override suspend fun passwordReset(email: String, password: String): PasswordResetStatus {
        return try {
            var status = true
            dbQuery {
                val user = EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first()

                if (user.password == password) {
                    status = false
                    return@dbQuery
                }

                user.password = password
            }

            if (status) PasswordResetStatus.SUCCESSFUL
            else PasswordResetStatus.SAME_AS_OLD_PASSWORD
        } catch (e: Exception) {
            PasswordResetStatus.SOMETHING_WENT_WRONG
        }
    }

    override suspend fun getUserProfilePic(email: String): File? {
        return try {
            dbQuery {
                val user = EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first()

                File(user.profilePic)
            }
        } catch (e: Exception) {
            null
        }
    }
}