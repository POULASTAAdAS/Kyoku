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
    private val db: LocalAuthDatasource,
    private val emailValidator: EmailVerificationUserCase,
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

                val user = db.createGoogleUser(
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

        return coroutineScope {
            val passHash = payload.password.encryptPassword() ?: return@coroutineScope AuthResponseDto()
            val countryIdDef = async { db.getCountryIdFromCountryCode(payload.countryCode) }
            // todo generate token from jwtRepo
            val tokenDef = async { Pair("", "") }

            val countryId = countryIdDef.await() ?: return@coroutineScope AuthResponseDto()
            val (refreshToken, accessToken) = tokenDef.await()

            val user = db.createEmailUser(
                user = ServerUserDto(
                    email = payload.email,
                    type = UserType.EMAIL,
                    username = payload.username,
                    password = passHash,
                    countryId = countryId,
                ),
                refreshToken = refreshToken
            )

            // todo send email verification mail

            AuthResponseDto(
                status = AuthResponseStatusDto.USER_CREATED,
                user = user.toUserDto(),
                token = JwtTokenDto(
                    refreshToken = refreshToken,
                    accessToken = accessToken
                )
            )
        }
    }

    override suspend fun emailSignIn(payload: EmailSignInPayload): AuthResponseDto {
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
            ) // todo send verification mail

            dbUser.bDate == null -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_FOUND_STORE_B_DATE,
                user = dbUser.toUserDto()
            ) // todo send email signIn verification mail

            else -> AuthResponseDto(
                status = AuthResponseStatusDto.USER_FOUND,
                user = dbUser.toUserDto()
            ) // todo send email signIn verification mail
        }
    }

    private suspend fun checkIfEmailUserAlreadyExists(payload: EmailSignUpPayload): AuthResponseDto? {
        val dbUser = db.getUsersByEmail(payload.email, UserType.EMAIL)

        return when {
            dbUser != null && !db.isEmailUserEmailVerified(dbUser.id) -> AuthResponseDto(
                status = AuthResponseStatusDto.EMAIL_NOT_VERIFIED,
                user = dbUser.toUserDto()
            ) // todo generate email verification mail

            dbUser != null -> AuthResponseDto(
                status = AuthResponseStatusDto.EMAIL_ALREADY_IN_USE
            )

            else -> null
        }
    }

    private fun String.encryptPassword(): String? = BCrypt.hashpw(this, BCrypt.gensalt(10))
    private fun verifyPassword(inputPassword: String, storedHash: String) = BCrypt.checkpw(inputPassword, storedHash)
}