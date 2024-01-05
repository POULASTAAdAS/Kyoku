package com.example.data.repository.user

import com.example.data.model.database.EmailAuthUserTable
import com.example.domain.model.EmailAuthUser
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.plugins.dbQuery
import com.example.routes.auth.common.EmailVerificationStatus
import com.example.routes.auth.common.UpdateEmailVerificationStatus
import com.example.routes.auth.common.UserCreationStatus
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.update

class EmailAuthUserRepositoryImpl : EmailAuthUserRepository {
    override suspend fun createUser(
        userName: String,
        email: String,
        password: String
    ): UserCreationStatus {
        return try {
            dbQuery {
                EmailAuthUser.new {
                    this.userName = userName
                    this.email = email
                    this.password = password
                }
            }
            UserCreationStatus.CREATED
        } catch (e: ExposedSQLException) {
            UserCreationStatus.CONFLICT
        } catch (e: Exception) {
            UserCreationStatus.INTERNAL_SERVER_ERROR
        }
    }

    override suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus {
        return try {
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
            println("error updating verification status: " + e.message)
            UpdateEmailVerificationStatus.SOMETHING_WENT_WRONG
        }
    }

    override suspend fun checkEmailVerification(email: String): EmailVerificationStatus {
        return try {
            dbQuery {
                EmailAuthUser.find {
                    EmailAuthUserTable.email eq email
                }.first().emailVerified
            }.run {
                if (this) EmailVerificationStatus.VERIFIED
                else EmailVerificationStatus.UN_VERIFIED
            }
        } catch (e: Exception) {
            EmailVerificationStatus.SOMETHING_WENT_WRONG
        }
    }
}