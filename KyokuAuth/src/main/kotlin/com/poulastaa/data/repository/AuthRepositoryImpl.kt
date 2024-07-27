package com.poulastaa.data.repository

import com.poulastaa.data.dao.other.LogInVerificationMail
import com.poulastaa.data.dao.user.EmailAuthUser
import com.poulastaa.data.dao.user.GoogleAuthUser
import com.poulastaa.data.model.User
import com.poulastaa.data.model.VerifiedMailStatus
import com.poulastaa.data.model.auth.res.Payload
import com.poulastaa.data.model.auth.response.EmailAuthRes
import com.poulastaa.data.model.auth.response.GoogleAuthRes
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.data.model.payload.GoogleAuthResPayload
import com.poulastaa.data.model.payload.UpdateEmailVerificationPayload
import com.poulastaa.data.model.payload.UpdatePasswordStatus
import com.poulastaa.domain.repository.AuthRepository
import com.poulastaa.domain.table.other.LogInVerificationMailTable
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.domain.table.user.GoogleAuthUserTable
import com.poulastaa.plugins.dbQuery
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

private typealias Email = String

class AuthRepositoryImpl : AuthRepository {
    private var logInVerificationMailJob: ConcurrentHashMap<Email, Job?> = ConcurrentHashMap()

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

    override suspend fun loginEmailUser(
        email: String,
        password: String,
        refreshToken: String,
    ): EmailAuthRes {
        val user = findEmailUser(email) ?: return EmailAuthRes(
            status = UserAuthStatus.USER_NOT_FOUND
        )

        val response = when {
            user.password != password -> EmailAuthRes(
                status = UserAuthStatus.PASSWORD_DOES_NOT_MATCH
            )

            !user.emailVerified -> EmailAuthRes(
                status = UserAuthStatus.EMAIL_NOT_VERIFIED
            )

            user.bDate == null -> EmailAuthRes(
                status = UserAuthStatus.USER_FOUND_STORE_B_DATE
            )

            else -> EmailAuthRes(
                status = UserAuthStatus.USER_FOUND_HOME,
                user = User(
                    email = user.email,
                    userName = user.userName,
                    profilePic = user.profilePic
                )
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            val entry = dbQuery {
                LogInVerificationMail.find {
                    LogInVerificationMailTable.email eq email
                }.singleOrNull()
            }

            if (entry == null) {  // create entry
                dbQuery {
                    LogInVerificationMail.new {
                        this.email = email
                    }
                }
            } else {  // cancel old job if any
                val jobEntry = logInVerificationMailJob[email]
                jobEntry?.cancel()
            }

            // start new delete job
            logInVerificationMailJob[email] = deleteLoginMailVerificationJobAfter10Minute(email)
        }

        return response
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


    override suspend fun updateSignUpEmailVerificationStatus(email: String): UpdateEmailVerificationPayload {
        val user = findEmailUser(email) ?: return UpdateEmailVerificationPayload()

        if (!user.emailVerified) dbQuery {
            user.emailVerified = true
        }

        return UpdateEmailVerificationPayload(
            username = user.userName,
            status = VerifiedMailStatus.VERIFIED
        )
    }

    override suspend fun updateLogInEmailVerificationStatus(email: String): UpdateEmailVerificationPayload {
        val user = findEmailUser(email) ?: return UpdateEmailVerificationPayload()

        val entry = dbQuery {
            LogInVerificationMail.find {
                LogInVerificationMailTable.email eq email
            }.singleOrNull()
        } ?: return UpdateEmailVerificationPayload()

        return if (!entry.isVerified) {
            try {
                dbQuery {
                    entry.isVerified = true
                }

                UpdateEmailVerificationPayload(
                    status = VerifiedMailStatus.VERIFIED,
                    username = user.userName
                )
            } catch (e: Exception) {
                UpdateEmailVerificationPayload(
                    status = VerifiedMailStatus.SOMETHING_WENT_WRONG
                )
            }
        } else {
            UpdateEmailVerificationPayload(
                status = VerifiedMailStatus.TOKEN_USED
            )
        }
    }

    override suspend fun signUpEmailVerificationCheck(email: String): Boolean {
        val user = findEmailUser(email) ?: return false

        return if (!user.emailVerificationDone && user.emailVerified) {
            dbQuery { user.emailVerificationDone = true }

            true
        } else false
    }

    override suspend fun logInEmailVerificationCheck(email: String): Boolean {
        val entry = dbQuery {
            LogInVerificationMail.find {
                LogInVerificationMailTable.email eq email
            }.singleOrNull()
        } ?: return false

        return if (entry.isVerified) {
            dbQuery { entry.delete() }

            true
        } else false
    }

    override suspend fun updatePassword(
        password: String,
        email: String,
    ): UpdatePasswordStatus {
        val user = findEmailUser(email) ?: return UpdatePasswordStatus.USER_NOT_FOUND
        if (password.length < 4 || password.length > 15) return UpdatePasswordStatus.NOT_PASSWORD

        if (password == user.password) return UpdatePasswordStatus.SAME_PASSWORD

        dbQuery {
            user.password = password
        }

        return UpdatePasswordStatus.RESET
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

    private fun deleteLoginMailVerificationJobAfter10Minute(
        email: String,
    ) = CoroutineScope(Dispatchers.IO).launch {
        delay(600000L) // 10 minute
        dbQuery {
            LogInVerificationMail.find {
                LogInVerificationMailTable.email eq email
            }.singleOrNull()?.delete()
        }

        logInVerificationMailJob[email]?.cancel()
        logInVerificationMailJob.remove(email)
    }
}