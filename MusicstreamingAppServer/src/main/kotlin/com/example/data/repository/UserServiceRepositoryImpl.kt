package com.example.data.repository

import com.example.data.model.EndPoints
import com.example.data.model.auth.res.*
import com.example.data.model.auth.stat.*
import com.example.domain.repository.UserServiceRepository
import com.example.domain.repository.jwt.JWTRepository
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.invalidTokenList
import com.example.util.Constants
import com.example.util.Constants.FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
import com.example.util.Constants.REFRESH_TOKEN_CLAIM_KEY
import com.example.util.Constants.VERIFICATION_MAIL_TOKEN_CLAIM_KEY
import com.example.util.constructProfileUrl
import com.example.util.sendEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class UserServiceRepositoryImpl(
    private val emailAuthUserRepository: EmailAuthUserRepository,
    private val jwtRepository: JWTRepository
) : UserServiceRepository {
    override suspend fun createUser(
        userName: String,
        email: String,
        password: String
    ): EmailSignInResponse {
        val accessToken = jwtRepository.generateAccessToken(email = email)
        val refreshToken = jwtRepository.generateRefreshToken(email = email)

        val status = emailAuthUserRepository.createUser(
            userName = userName,
            email = email,
            password = password,
            refreshToken = refreshToken
        )

        return when (status) {
            UserCreationStatus.CREATED -> {
                val verificationMailToken = jwtRepository.generateVerificationMailToken(email = email)

                CoroutineScope(Dispatchers.IO).launch {
                    sendEmail( // conform email
                        to = email,
                        subject = "Authentication Mail",
                        content = (
                                (
                                        "<html>"
                                                + "<body>"
                                                + "<h1>Email Authentication</h1>"
                                                + "<p>Click the following link to authenticate your email:</p>"
                                                + "<a href=\"${Constants.BASE_URL + EndPoints.VerifyEmail.route}?token=" + verificationMailToken
                                        ) + "\">Authenticate</a>"
                                        + "</body>"
                                        + "</html>"
                                )
                    )
                }

                EmailSignInResponse(
                    status = status,
                    userName = userName,
                    accessToken = accessToken,
                    refreshToken = refreshToken,
                    profilePic = constructProfileUrl()
                )
            }

            UserCreationStatus.CONFLICT -> {
                EmailSignInResponse(
                    status = status
                )
            }

            UserCreationStatus.SOMETHING_WENT_WRONG -> {
                EmailSignInResponse(
                    status = status
                )
            }

            UserCreationStatus.EMAIL_NOT_VALID -> throw IllegalArgumentException("invalid")
        }
    }

    override suspend fun updateVerificationStatus(token: String): UpdateEmailVerificationStatus {
        jwtRepository.verifyJWTToken(
            token,
            VERIFICATION_MAIL_TOKEN_CLAIM_KEY
        ).let {
            if (it == null) return UpdateEmailVerificationStatus.TOKEN_NOT_VALID

            if (it == UpdateEmailVerificationStatus.TOKEN_USED.name)
                return UpdateEmailVerificationStatus.TOKEN_USED

            invalidTokenList.add(token)

            return emailAuthUserRepository.updateVerificationStatus(it)
        }
    }


    override suspend fun checkEmailVerification(email: String): EmailVerificationResponse =
        EmailVerificationResponse(status = emailAuthUserRepository.checkEmailVerification(email))

    override suspend fun loginUser(email: String, password: String): EmailLoginResponse {
        val accessToken = jwtRepository.generateAccessToken(email = email)
        val refreshToken = jwtRepository.generateRefreshToken(email = email)

        val response = emailAuthUserRepository.loginUser(
            email = email,
            password = password,
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        if (response.status == EmailLoginStatus.USER_PASS_MATCHED) {
            CoroutineScope(Dispatchers.IO).launch {
                sendEmail(
                    to = email,
                    subject = "Welcome back ${response.userName}",
                    content = "It's fantastic to see you back with us!\n We've missed you and are thrilled to have you back.\n" +
                            "We hope you're ready for an exciting journey ahead." +
                            "Here's to making great memories and having a blast together!\n" +
                            "Best regards,\n From Kyoku :)" //todo add html
                )
            }
        }

        return response
    }

    override suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail {
        return when (
            val status = emailAuthUserRepository
                .checkIfUSerExistsToSendForgotPasswordMail(email)
        ) {
            SendVerificationMailStatus.USER_EXISTS -> {
                val token = jwtRepository.generateForgotPasswordMailToken(email = email)

                val content = (
                        (
                                "<html>"
                                        + "<body>"
                                        + "<h1>Do not share this email</h1>"
                                        + "<p>Click the following link to reset your password:</p>"
                                        + "<a href=\"${Constants.BASE_URL + EndPoints.ResetPassword.route}?token=" + token
                                ) + "\">Reset Password</a>" // todo add js and textField
                                + "</body>"
                                + "</html>"
                        )

                if (
                    sendEmail(
                        to = email,
                        subject = "Reset password mail",
                        content = content
                    )
                ) {
                    SendForgotPasswordMail(
                        status = status
                    )
                } else {
                    SendForgotPasswordMail(
                        status = SendVerificationMailStatus.SOMETHING_WENT_WRONG
                    )
                }
            }

            SendVerificationMailStatus.USER_NOT_FOUND -> {
                SendForgotPasswordMail(
                    status = status
                )
            }

            SendVerificationMailStatus.SOMETHING_WENT_WRONG -> {
                SendForgotPasswordMail(
                    status = status
                )
            }
        }
    }

    override suspend fun resetPassword(
        token: String,
        password: String
    ): PasswordResetStatus {
        jwtRepository.verifyJWTToken(
            token = token,
            claim = FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
        ).let {
            if (it == null) return PasswordResetStatus.TOKEN_EXPIRED

            if (it == PasswordResetStatus.TOKEN_USED.name) return PasswordResetStatus.TOKEN_USED

            invalidTokenList.add(token)
            return emailAuthUserRepository.resetPassword(
                email = it,
                password = password
            )
        }
    }

    override suspend fun getUserProfilePic(email: String): File? =
        emailAuthUserRepository.getUserProfilePic(email)

    override suspend fun refreshToken(token: String): RefreshTokenResponse {
        val email = jwtRepository.verifyJWTToken(token, REFRESH_TOKEN_CLAIM_KEY) ?: return RefreshTokenResponse(
            status = RefreshTokenUpdateStatus.TOKEN_EXPIRED
        )

        val accessToken = jwtRepository.generateAccessToken(email = email)
        val refreshToken = jwtRepository.generateRefreshToken(email = email)

        val status = emailAuthUserRepository.updateRefreshToken(
            email = email,
            oldRefreshToken = token,
            refreshToken = refreshToken
        )

        return when (status) {
            RefreshTokenUpdateStatus.UPDATED -> RefreshTokenResponse(
                status = status,
                accessToken = accessToken,
                refreshToken = refreshToken
            )

            RefreshTokenUpdateStatus.USER_NOT_FOUND -> RefreshTokenResponse(
                status = status,
            )

            RefreshTokenUpdateStatus.SOMETHING_WENT_WRONG -> RefreshTokenResponse(
                status = status,
            )

            RefreshTokenUpdateStatus.DUPLICATE_TOKEN -> RefreshTokenResponse(
                status = status
            )

            RefreshTokenUpdateStatus.TOKEN_EXPIRED -> throw IllegalArgumentException()
        }
    }
}