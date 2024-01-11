package com.example.data.repository

import com.example.data.model.EndPoints
import com.example.data.model.auth.res.EmailLoginResponse
import com.example.data.model.auth.res.EmailSignInResponse
import com.example.data.model.auth.res.EmailVerificationResponse
import com.example.data.model.auth.stat.*
import com.example.domain.repository.UserServiceRepository
import com.example.domain.repository.jwt.JWTRepository
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.invalidTokenList
import com.example.util.Constants
import com.example.util.Constants.FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
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

        return emailAuthUserRepository.loginUser(
            email = email,
            password = password,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }

    override suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail {
        return when (
            val status = emailAuthUserRepository
                .checkIfUSerExistsThenSendForgotPasswordMail(email)
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

    override suspend fun getUserProfilePic(email: String): File? {
        TODO("Not yet implemented")
    }
}