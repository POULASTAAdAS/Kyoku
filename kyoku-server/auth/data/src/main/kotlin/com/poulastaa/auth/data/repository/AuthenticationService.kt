package com.poulastaa.auth.data.repository

import com.poulastaa.auth.data.mapper.toUserDto
import com.poulastaa.auth.domain.model.*
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mindrot.jbcrypt.BCrypt

class AuthenticationService(
    private val emailValidator: EmailVerificationUserCase,
    private val db: LocalAuthDatasource,
) : AuthRepository {
    override suspend fun googleAuth(
        payload: GoogleAuthPayloadDto,
        countryCode: String,
    ): AuthResponseDto = coroutineScope {
        val countryIdDef = async { db.getCountryIdFromCountryCode(countryCode) }
        val userDef = async { db.getUsersByEmail(payload.email, UserType.GOOGLE) }

        val countryId = countryIdDef.await() ?: return@coroutineScope AuthResponseDto()
        val user = userDef.await()

        if (user != null &&
            !verifyPassword(payload.sub, user.passwordHash)
        ) return@coroutineScope AuthResponseDto(
            status = AuthResponseStatusDto.PASSWORD_DOES_NOT_MATCH
        )

        when {
            user == null -> {
                val passHash = payload.sub.encryptPassword() ?: return@coroutineScope AuthResponseDto()

                val user = db.createUser(
                    user = ServerUserDto(
                        email = payload.email,
                        type = UserType.GOOGLE,
                        username = payload.userName,
                        password = passHash,
                        profilePicUrl = payload.profilePicUrl,
                        countryId = countryId,
                    )
                )

                // todo send welcome mail

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_CREATED,
                    user = user.toUserDto()
                )
            }

            user.bDate == null -> AuthResponseDto( // todo send welcome-back mail
                status = AuthResponseStatusDto.USER_FOUND_STORE_B_DATE,
                user = user.toUserDto()
            )

            else -> AuthResponseDto( // todo send welcome-back mail
                status = AuthResponseStatusDto.USER_FOUND,
                user = user.toUserDto()
            )
        }
    }

    override suspend fun emailSignUp(payload: EmailSignUpPayload): AuthResponseDto {
        if (!emailValidator.isValidEmail(payload.email)) return AuthResponseDto(
            status = AuthResponseStatusDto.INVALID_EMAIL
        )

        checkIfEmailUserAlreadyExists(payload)?.let { return it }

        val passHash = payload.password.encryptPassword() ?: return AuthResponseDto()
        val countryId = db.getCountryIdFromCountryCode(payload.countryCode) ?: return AuthResponseDto()

        val user = db.createUser(
            user = ServerUserDto(
                email = payload.email,
                type = UserType.EMAIL,
                username = payload.username,
                password = passHash,
                countryId = countryId,
            )
        )

        // todo send email verification mail

        return AuthResponseDto(
            status = AuthResponseStatusDto.USER_CREATED,
            user = user.toUserDto()
        )
    }

    override suspend fun emailLogIn(payload: EmailLogInPayload): AuthResponseDto {
        if (!emailValidator.isValidEmail(payload.email)) return AuthResponseDto(
            status = AuthResponseStatusDto.INVALID_EMAIL
        )

        val dbUser = db.getUsersByEmail(payload.email, UserType.EMAIL)

        return when {
            dbUser == null -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_NOT_FOUND
            )

            verifyPassword(payload.password, dbUser.passwordHash) == false -> AuthResponseDto(
                status = AuthResponseStatusDto.PASSWORD_DOES_NOT_MATCH
            )

            !db.isEmailUserEmailVerified(dbUser.id) -> AuthResponseDto(
                status = AuthResponseStatusDto.EMAIL_NOT_VERIFIED,
                user = dbUser.toUserDto()
            ) // todo send email verification mail

            dbUser.bDate == null -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_FOUND_STORE_B_DATE,
                user = dbUser.toUserDto()
            ) // todo send logIn mail

            else -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_FOUND,
                user = dbUser.toUserDto()
            ) // todo send logIn mail
        }
    }

    private suspend fun checkIfEmailUserAlreadyExists(payload: EmailSignUpPayload): AuthResponseDto? {
        val dbUser = db.getUsersByEmail(payload.email, UserType.EMAIL)

        return when {
            dbUser != null && !db.isEmailUserEmailVerified(dbUser.id) -> AuthResponseDto(
                status = AuthResponseStatusDto.EMAIL_NOT_VERIFIED,
                user = dbUser.toUserDto()
            ) // todo send email verification mail

            dbUser != null -> AuthResponseDto(
                status = AuthResponseStatusDto.EMAIL_ALREADY_IN_USE
            )

            else -> null
        }
    }

    private fun String.encryptPassword(): String? = BCrypt.hashpw(this, BCrypt.gensalt(10))
    private fun verifyPassword(inputPassword: String, storedHash: String) = BCrypt.checkpw(inputPassword, storedHash)
}