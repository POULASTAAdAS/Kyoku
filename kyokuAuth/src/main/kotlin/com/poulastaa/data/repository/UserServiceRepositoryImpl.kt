package com.poulastaa.data.repository


import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.auth.jwt.*
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.domain.dao.PasskeyAuthUser
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.jwt.JWTRepository
import com.poulastaa.domain.repository.user_db.EmailAuthUserRepository
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import com.poulastaa.invalidTokenList
import com.poulastaa.utils.Constants
import com.poulastaa.utils.Constants.FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.REFRESH_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.VERIFICATION_MAIL_TOKEN_CLAIM_KEY
import com.poulastaa.utils.constructProfileUrl
import com.poulastaa.utils.sendEmail
import com.poulastaa.utils.toPasskeyAuthResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class UserServiceRepositoryImpl(
    private val jwtRepository: JWTRepository,
    private val emailAuthUser: EmailAuthUserRepository,
    private val googleAuthUser: GoogleAuthUserRepository,
    private val passkeyAuthUser: PasskeyAuthUserRepository
) : UserServiceRepository {
    override suspend fun createEmailUser(
        userName: String,
        email: String,
        password: String
    ): EmailSignInResponse {
        val accessToken = jwtRepository.generateAccessToken(email = email)
        val refreshToken = jwtRepository.generateRefreshToken(email = email)

        val status = emailAuthUser.createUser(
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
                    user = User(
                        userName = userName,
                        profilePic = constructProfileUrl()
                    ),
                    accessToken = accessToken,
                    refreshToken = refreshToken,
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

            UserCreationStatus.USER_NOT_FOUND -> throw IllegalArgumentException("invalid")

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

            return emailAuthUser.updateVerificationStatus(it)
        }
    }


    override suspend fun checkEmailVerification(email: String): EmailVerificationResponse =
        EmailVerificationResponse(status = emailAuthUser.checkEmailVerification(email))

    override suspend fun getEmailUser(email: String, password: String): EmailLoginResponse {
        val accessToken = jwtRepository.generateAccessToken(email = email)
        val refreshToken = jwtRepository.generateRefreshToken(email = email)

        val response = emailAuthUser.loginUser(
            email = email,
            password = password,
            accessToken = accessToken,
            refreshToken = refreshToken
        )

        if (response.status == EmailLoginStatus.USER_PASS_MATCHED) {
            CoroutineScope(Dispatchers.IO).launch {
                sendLogInMail(
                    to = email,
                    userName = response.user.userName
                )
            }
        }

        return response
    }

    override suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail {
        return when (
            val status = emailAuthUser
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
            return emailAuthUser.resetPassword(
                email = it,
                password = password
            )
        }
    }

    override suspend fun refreshToken(token: String): RefreshTokenResponse {
        val email = jwtRepository.verifyJWTToken(token, REFRESH_TOKEN_CLAIM_KEY) ?: return RefreshTokenResponse(
            status = RefreshTokenUpdateStatus.TOKEN_EXPIRED
        )

        val accessToken = jwtRepository.generateAccessToken(email = email)
        val refreshToken = jwtRepository.generateRefreshToken(email = email)

        val status = emailAuthUser.updateRefreshToken(
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

    override suspend fun getUserProfilePic(email: String): File? =
        emailAuthUser.getUserProfilePic(email)

    // google user service
    override suspend fun createGoogleUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): GoogleAuthResponse {
        val response = googleAuthUser.createUser(
            userName = userName,
            email = email,
            sub = sub,
            pictureUrl = pictureUrl
        )

        CoroutineScope(Dispatchers.IO).launch {
            if (response.status == UserCreationStatus.CREATED) {
                createMail(email, response.user.userName)
            } else if (response.status == UserCreationStatus.CONFLICT) {
                sendLogInMail(
                    to = email,
                    userName = response.user.userName
                )
            }
        }

        return response
    }

    override suspend fun findUserByEmail(email: String): PasskeyAuthUser? =
        passkeyAuthUser.findUserByEmail(email)

    override suspend fun createPasskeyUser(
        userName: String,
        email: String,
        userId: String
    ): PasskeyAuthResponse {
        val response = passkeyAuthUser.createUser(
            userId = userId,
            email = email,
            userName = userName
        )

        CoroutineScope(Dispatchers.IO).launch {
            when (response.status) {
                UserCreationStatus.CREATED -> {
                    createMail(
                        email = email,
                        userName = response.user.userName
                    )
                }

                UserCreationStatus.CONFLICT -> {
                    sendLogInMail(
                        to = email,
                        userName = response.user.userName
                    )
                }

                else -> Unit
            }
        }

        return response
    }

    override suspend fun getPasskeyUser(userId: String): PasskeyAuthResponse {
        val user = passkeyAuthUser.getUser(userId)

        if (user != null) {
            sendLogInMail(
                to = user.email,
                userName = user.displayName
            )
            return user.toPasskeyAuthResponse(status = UserCreationStatus.CONFLICT)
        }

        return PasskeyAuthResponse(
            status = UserCreationStatus.USER_NOT_FOUND
        )
    }


    private fun createMail(
        email: String,
        userName: String
    ) {
        sendEmail(
            to = email,
            subject = "Welcome $userName",
            content = (
                    "<html>"
                            + "<body>"
                            + "<h1>Welcome to Kyoku :)</h1>"
                            + "<br>"
                            + "<p>Listen to millions of songs add free</p>"
                            + "<p>Download as much songs as you want</p>"
                            + "<br>"
                            + "<p>This app is developed by only one Developer :)</p>"
                            + "<p>If it contains any bug please mail me on this email</p>"
                            + "<p>-> ${System.getenv("email")} <-</p>"
                            + "<p>Hope You have a great experience with Kyoku</p>"
                            + "<p>Thank You :)</p>"
                            + "</body>"
                            + "</html>"
                    )
        )
    }

    private fun sendLogInMail(
        to: String,
        userName: String
    ) {
        sendEmail(
            to = to,
            subject = "Welcome back $userName",
            content = "It's fantastic to see you back with us!\n" +
                    "We've missed you and are thrilled to have you back.\n" +
                    "We hope you're ready for an exciting journey ahead." +
                    "Here's to making great memories and having a blast together!\n" +
                    "Best regards,\n From Kyoku :)" //todo add html
        )
    }
}