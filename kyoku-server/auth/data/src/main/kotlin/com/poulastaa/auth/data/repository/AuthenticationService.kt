package com.poulastaa.auth.data.repository

import com.poulastaa.auth.data.mapper.toUserDto
import com.poulastaa.auth.domain.model.*
import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.JWTRepository
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.mindrot.jbcrypt.BCrypt

class AuthenticationService(
    private val emailValidator: EmailVerificationUserCase,
    private val db: LocalAuthDatasource,
    private val jwt: JWTRepository,
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

                db.sendMail(
                    message = Pair(
                        first = MailType.WELCOME,
                        second = "${user.email},${user.userName}"
                    )
                )

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_CREATED,
                    user = user.toUserDto()
                )
            }

            user.bDate == null -> {
                db.sendMail(
                    message = Pair(
                        first = MailType.WELCOME_BACK,
                        second = "${user.email},${user.userName}"
                    )
                )

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_FOUND_STORE_B_DATE,
                    user = user.toUserDto()
                )
            }

            else -> {
                db.sendMail(
                    message = Pair(
                        first = MailType.WELCOME_BACK,
                        second = "${user.email},${user.userName}"
                    )
                )

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_FOUND,
                    user = user.toUserDto()
                )
            }
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

        db.sendMail(
            message = Pair(
                first = MailType.EMAIL_VERIFICATION,
                second = user.email
            )
        )

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

            dbUser.bDate == null -> {
                db.sendMail(
                    message = Pair(
                        first = MailType.EMAIL_VERIFICATION,
                        second = payload.email
                    )
                )

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_FOUND_STORE_B_DATE,
                    user = dbUser.toUserDto()
                )
            }

            else -> {
                db.sendMail(
                    message = Pair(
                        first = MailType.EMAIL_VERIFICATION,
                        second = payload.email
                    )
                )

                AuthResponseDto(
                    status = AuthResponseStatusDto.USER_FOUND,
                    user = dbUser.toUserDto()
                )
            }
        }
    }

    override suspend fun verifyEmail(token: String): EmailVerificationStatusDto {
        if (db.isVerificationTokenUsed(token)) return EmailVerificationStatusDto.TOKEN_USED

        val email = jwt.verifyToken(token, JWTRepository.TokenType.TOKEN_VERIFICATION_MAIL)
            ?: return EmailVerificationStatusDto.SERVER_ERROR

        db.storeVerificationToken(token)

        val status = db.updateVerificationMailStatus(email)
            ?: return EmailVerificationStatusDto.USER_NOT_FOUND

        return if (status) EmailVerificationStatusDto.VERIFIED
        else EmailVerificationStatusDto.TOKEN_USED
    }

    override suspend fun checkEmailVerificationState(email: String): JwtTokenDto? {
        val state = db.getEmailVerificationState(email)

        return if (state) {
            val refreshToken = jwt.generateToken(
                email = email,
                type = JWTRepository.TokenType.TOKEN_REFRESH
            )

            val accessToken = jwt.generateToken(
                email = email,
                type = JWTRepository.TokenType.TOKEN_ACCESS
            )

            db.saveRefreshToken(refreshToken, email)

            JwtTokenDto(
                refreshToken = refreshToken,
                accessToken = accessToken
            )
        } else null
    }

    override suspend fun forgotPassword(email: String): ForgotPasswordResponseStatusDto {
        if (!emailValidator.isValidEmail(email)) return ForgotPasswordResponseStatusDto.EMAIL_NOT_VALID
        val user = db.getUsersByEmail(email, UserType.EMAIL) ?: return ForgotPasswordResponseStatusDto.USER_NOT_FOUND

        db.sendMail(
            message = Pair(
                first = MailType.EMAIL_VERIFICATION,
                second = user.email
            )
        )

        return ForgotPasswordResponseStatusDto.FORGOT_PASSWORD_MAIL_SEND
    }

    override suspend fun updatePassword(token: String, newPassword: String): UpdatePasswordStatusDto {
        if (db.isResetPasswordTokenUsed(token)) return UpdatePasswordStatusDto.TOKEN_USED
        val email = jwt.verifyToken(token, JWTRepository.TokenType.TOKEN_SUBMIT_NEW_PASSWORD)
            ?: return UpdatePasswordStatusDto.TOKEN_USED

        val user = db.getUsersByEmail(email, UserType.EMAIL) ?: return UpdatePasswordStatusDto.USER_NOT_FOUND
        if (verifyPassword(newPassword, user.passwordHash)) return UpdatePasswordStatusDto.SAME_PASSWORD

        db.updatePassword(email, newPassword.encryptPassword() ?: return UpdatePasswordStatusDto.SERVER_ERROR)

        return UpdatePasswordStatusDto.RESET
    }

    private suspend fun checkIfEmailUserAlreadyExists(payload: EmailSignUpPayload): AuthResponseDto? {
        val dbUser = db.getUsersByEmail(payload.email, UserType.EMAIL)

        return when {
            dbUser != null -> AuthResponseDto(
                status = AuthResponseStatusDto.EMAIL_ALREADY_IN_USE
            )

            else -> null
        }
    }

    private fun String.encryptPassword(): String? = BCrypt.hashpw(this, BCrypt.gensalt(10))
    private fun verifyPassword(inputPassword: String, storedHash: String) = BCrypt.checkpw(inputPassword, storedHash)
}