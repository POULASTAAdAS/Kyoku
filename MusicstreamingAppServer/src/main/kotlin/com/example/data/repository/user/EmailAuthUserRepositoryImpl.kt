package com.example.data.repository.user

import com.example.data.model.EmailLoginResponse
import com.example.data.model.EmailSignUpResponse
import com.example.data.model.SendVerificationMail
import com.example.data.model.UserCreationResponse
import com.example.data.model.database.EmailAuthUserTable
import com.example.domain.model.EmailAuthUser
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.plugins.dbQuery
import com.example.routes.auth.common.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update

class EmailAuthUserRepositoryImpl : EmailAuthUserRepository {
    override suspend fun createUser(
        userName: String,
        email: String,
        password: String
    ): UserCreationResponse = try {
        dbQuery {
            EmailAuthUser.new {
                this.userName = userName
                this.email = email
                this.password = password
            }
        }
        UserCreationResponse(
            status = UserCreationStatus.CREATED
        )
    } catch (e: ExposedSQLException) {
        UserCreationResponse(
            status = UserCreationStatus.CONFLICT
        )
    } catch (e: Exception) {
        UserCreationResponse(
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
    override suspend fun checkEmailVerification(email: String): EmailSignUpResponse =
        try {
            dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first().emailVerified
            }.run {
                if (this) EmailSignUpResponse(
                    status = EmailVerificationStatus.VERIFIED
                )
                else EmailSignUpResponse(
                    status = EmailVerificationStatus.UN_VERIFIED
                )
            }
        } catch (e: Exception) {
            EmailSignUpResponse(
                status = EmailVerificationStatus.SOMETHING_WENT_WRONG
            )
        }


    override suspend fun loginUser(
        email: String,
        password: String
    ): EmailLoginResponse {
        return try {
            val user = dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first()
            }

            if (user.password == password) {
                if (!user.emailVerified)
                    EmailLoginResponse(
                        status = EmailLoginStatus.EMAIL_NOT_VERIFIED
                    )
                else
                    EmailLoginResponse(
                        status = EmailLoginStatus.USER_PASS_MATCHED //todo add other data to response
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
            println("error finding user: $e")
            EmailLoginResponse(
                status = EmailLoginStatus.SOMETHING_WENT_WRONG
            )
        }
    }

    override suspend fun checkIfUserExists(email: String, password: String): SendVerificationMail {
        return try {
            dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email and (EmailAuthUserTable.password eq password)
                }
            }

            SendVerificationMail(
                status = SendVerificationMailStatus.USER_EXISTS
            )
        } catch (e: Exception) {
            SendVerificationMail(
                status = SendVerificationMailStatus.SOMETHING_WENT_WRONG
            )
        }
    }
}