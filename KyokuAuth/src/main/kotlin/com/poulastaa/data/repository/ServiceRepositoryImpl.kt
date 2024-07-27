package com.poulastaa.data.repository

import com.poulastaa.data.dao.other.Country
import com.poulastaa.data.dao.user.EmailAuthUser
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.User
import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.VerifiedMailStatus
import com.poulastaa.data.model.auth.req.EmailLogInReq
import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.res.Payload
import com.poulastaa.data.model.auth.response.*
import com.poulastaa.data.model.payload.UpdatePasswordStatus
import com.poulastaa.domain.repository.*
import com.poulastaa.domain.table.other.CountryTable
import com.poulastaa.domain.table.user.EmailAuthUserTable
import com.poulastaa.invalidTokenList
import com.poulastaa.plugins.dbQuery
import com.poulastaa.utils.Constants.FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.GET_LOGIN_DATA_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.SUBMIT_NEW_PASSWORD_TOKEN_CLAIM_KEY
import com.poulastaa.utils.Constants.VERIFICATION_MAIL_TOKEN_CLAIM_KEY
import com.poulastaa.utils.constructProfileUrl
import com.poulastaa.utils.sendEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.upperCase

class ServiceRepositoryImpl(
    private val authRepo: AuthRepository,
    private val jwtRepo: JWTRepository,
) : ServiceRepository {
    private val countryListMap = mapOf(
        "In" to "India",
        "US" to "United States"
    )

    override suspend fun createEmailUser(req: EmailSignUpReq): EmailAuthRes {
        if (!validateEmail(req.email)) return EmailAuthRes(
            status = UserAuthStatus.EMAIL_NOT_VALID
        )

        val country = req.countryCode.getCountryId() ?: return EmailAuthRes(
            status = UserAuthStatus.SOMETHING_WENT_WRONG
        )

        val refreshToken = jwtRepo.generateRefreshToken(email = req.email)

        val status = authRepo.createEmailUser(
            userName = req.userName,
            email = req.email,
            password = req.password,
            refreshToken = refreshToken,
            countryId = country
        )

        return when (status) {
            UserAuthStatus.CREATED -> {
                val verificationMailToken = jwtRepo.generateVerificationMailToken(email = req.email)

                CoroutineScope(Dispatchers.IO).launch {
                    sendEmailSignUpVerificationMail(
                        toEmail = req.email,
                        verificationMailToken = verificationMailToken
                    )
                }

                EmailAuthRes(
                    status = status,
                    user = User(
                        email = req.email,
                        userName = req.userName,
                        profilePic = constructProfileUrl()
                    )
                )
            }

            else -> {
                EmailAuthRes(
                    status = status
                )
            }
        }
    }

    override suspend fun loginEmailUser(req: EmailLogInReq): EmailAuthRes {
        val refreshToken = jwtRepo.generateRefreshToken(email = req.email)

        var response = authRepo.loginEmailUser(
            email = req.email,
            password = req.password,
            refreshToken = refreshToken
        )

        when (response.status) {
            UserAuthStatus.USER_FOUND_HOME,
            UserAuthStatus.USER_FOUND_SET_ARTIST,
            UserAuthStatus.USER_FOUND_SET_GENRE,
            UserAuthStatus.USER_FOUND_STORE_B_DATE,
            -> {
                val homeData = getLogInData(
                    email = req.email,
                    userType = UserType.EMAIL_USER,
                    authKey = System.getenv("authKey")
                )

                response = response.copy(
                    // todo
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val verificationMailToken = jwtRepo.generateVerificationMailToken(email = req.email)

                    sendEmailLogInVerificationMail(
                        toEmail = req.email,
                        verificationMailToken = verificationMailToken,
                    )
                }
            }

            UserAuthStatus.EMAIL_NOT_VERIFIED -> {
                val verificationMailToken = jwtRepo.generateVerificationMailToken(email = req.email)

                sendEmailSignUpVerificationMail(
                    toEmail = req.email,
                    verificationMailToken = verificationMailToken,
                )
            }

            else -> Unit
        }

        return response
    }

    override suspend fun googleAuth(payload: Payload, countryCode: String): Pair<GoogleAuthRes, UserId> {
        val country = countryCode.getCountryId() ?: return GoogleAuthRes(
            status = UserAuthStatus.SOMETHING_WENT_WRONG
        ) to -1

        val response = authRepo.googleAuth(payload, country)

        return when (response.response.status) {
            UserAuthStatus.CREATED -> {
                CoroutineScope(Dispatchers.IO).launch {
                    welcomeMail(
                        email = payload.email,
                        userName = payload.userName
                    )
                }

                response.response to response.userId
            }

            UserAuthStatus.USER_FOUND_STORE_B_DATE,
            UserAuthStatus.USER_FOUND_SET_GENRE,
            UserAuthStatus.USER_FOUND_SET_ARTIST,
            -> response.response to response.userId

            UserAuthStatus.USER_FOUND_HOME -> {
                CoroutineScope(Dispatchers.IO).launch {
                    welcomeBackMail(
                        to = payload.email,
                        userName = payload.userName
                    )
                }

                response.response to response.userId
            }

            else -> GoogleAuthRes() to -1
        }
    }

    override suspend fun updateSignUpEmailVerificationStatus(token: String): VerifiedMailStatus {
        val result = jwtRepo.verifyJWTToken(
            token = token,
            claim = VERIFICATION_MAIL_TOKEN_CLAIM_KEY
        ) ?: return VerifiedMailStatus.TOKEN_NOT_VALID

        if (result == VerifiedMailStatus.TOKEN_USED.name) return VerifiedMailStatus.TOKEN_USED

        invalidTokenList.add(token)

        val response = authRepo.updateSignUpEmailVerificationStatus(result)

        when (response.status) {
            VerifiedMailStatus.VERIFIED -> {
                CoroutineScope(Dispatchers.IO).launch {

                    welcomeMail(
                        email = result,
                        userName = response.username
                    )
                }
            }

            else -> Unit
        }

        return response.status
    }

    override suspend fun updateLogInEmailVerificationStatus(token: String): VerifiedMailStatus {
        val result = jwtRepo.verifyJWTToken(
            token = token,
            claim = VERIFICATION_MAIL_TOKEN_CLAIM_KEY
        ) ?: return VerifiedMailStatus.TOKEN_NOT_VALID

        if (result == VerifiedMailStatus.TOKEN_USED.name) return VerifiedMailStatus.TOKEN_USED

        invalidTokenList.add(token)

        val response = authRepo.updateLogInEmailVerificationStatus(result)

        when (response.status) {
            VerifiedMailStatus.VERIFIED -> {
                CoroutineScope(Dispatchers.IO).launch {

                    welcomeBackMail(
                        to = result,
                        userName = response.username
                    )
                }
            }

            else -> Unit
        }

        return response.status
    }

    override suspend fun signUpEmailVerificationCheck(email: String): CheckEmailVerificationResponse {
        val response = authRepo.signUpEmailVerificationCheck(email)
        return emailVerificationCheckResponse(response, email)
    }

    override suspend fun logInEmailVerificationCheck(email: String): CheckEmailVerificationResponse {
        val response = authRepo.logInEmailVerificationCheck(email)
        return emailVerificationCheckResponse(response, email)
    }

    override suspend fun sendForgotPasswordMail(email: String): ForgotPasswordSetStatus {
        dbQuery {
            EmailAuthUser.find {
                EmailAuthUserTable.email eq email
            }.singleOrNull()
        } ?: return ForgotPasswordSetStatus.NO_USER_FOUND

        val token = jwtRepo.generateForgotPasswordMailToken(email = email)

        forgotPasswordMail(
            to = email,
            token = token
        )

        return ForgotPasswordSetStatus.SENT
    }

    override suspend fun validateForgotPasswordMailToken(token: String): Pair<Email, VerifiedMailStatus> {
        val result = jwtRepo.verifyJWTToken(
            token = token,
            claim = FORGOT_PASSWORD_MAIL_TOKEN_CLAIM_KEY
        ) ?: return "" to VerifiedMailStatus.TOKEN_NOT_VALID

        if (result == VerifiedMailStatus.TOKEN_USED.name) return "" to VerifiedMailStatus.TOKEN_USED
        invalidTokenList.add(token)

        val newToken = jwtRepo.generateSubmitPasswordVerificationToken(email = result)
        return newToken to VerifiedMailStatus.VERIFIED
    }

    override suspend fun updatePassword(
        token: String,
        password: String,
    ): UpdatePasswordStatus {
        val result = jwtRepo.verifyJWTToken(
            token = token,
            claim = SUBMIT_NEW_PASSWORD_TOKEN_CLAIM_KEY
        ) ?: return UpdatePasswordStatus.USER_NOT_FOUND

        if (result == VerifiedMailStatus.TOKEN_USED.name) return UpdatePasswordStatus.TOKEN_USED

        val response = authRepo.updatePassword(
            password = password.trim(),
            email = result
        )

        if (response != UpdatePasswordStatus.SAME_PASSWORD)
            invalidTokenList.add(token)

        return response
    }

    private suspend fun emailVerificationCheckResponse(
        response: Boolean,
        email: String,
    ) = if (response) {
        val accessToken = jwtRepo.generateAccessToken(email = email)
        val refreshToken = dbQuery {
            EmailAuthUser.find {
                EmailAuthUserTable.email eq email
            }.single().refreshToken
        }

        CheckEmailVerificationResponse(
            status = true,
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    } else CheckEmailVerificationResponse()

    private fun validateEmail(email: String) =
        email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$"))

    private fun String.countryCodeToCountry(): String? {
        countryListMap.forEach { (key, value) ->
            if (key.uppercase() == this.uppercase()) return value
        }

        return null
    }

    private suspend fun String.getCountryId(): Int? = dbQuery {
        val country = this.countryCodeToCountry()?.uppercase() ?: return@dbQuery null

        Country.find {
            CountryTable.name.upperCase() eq country
        }.singleOrNull()?.id?.value
    }


    private fun sendEmailSignUpVerificationMail(
        toEmail: String,
        verificationMailToken: String,
    ) {
        val verificationLink =
            "${System.getenv("AUTH_URL") + EndPoints.VerifySignUpEmail.route}?token=$verificationMailToken"
        val content = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Verify Your Email</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    color: #333;
                    line-height: 1.6;
                }
                .container {
                    width: 80%;
                    margin: 0 auto;
                    padding: 20px;
                    background-color: #f4f4f4;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }
                .appLogo img {
                    width: 150px;
                    margin-bottom: 20px;
                    display: block;
                    margin-left: auto;
                    margin-right: auto;
                }
                .header {
                    color: #4CAF50;
                    text-align: center;
                }
                .content {
                    margin: 20px 0;
                    text-align: center;
                }
                .cta-button {
                    display: inline-block;
                    padding: 10px 20px;
                    color: white;
                    background-color: #4CAF50;
                    text-decoration: none;
                    border-radius: 5px;
                    margin-top: 20px;
                }
                .footer {
                    margin-top: 20px;
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <p class="appLogo">
                    <a>
                        <img src="${System.getenv("AUTH_URL")}/.well-known/app_logo.png" alt="Kyoku Logo">
                    </a>
                </p>
            
                <h1 class="header">Welcome to Kyoku!</h1>
                <p class="content">Thank you for signing up. To complete your registration, please verify your email address by clicking the link below:</p>
                <p class="content">
                    <a href="$verificationLink" class="cta-button">Verify Email</a>
                </p>
                <p class="footer">If you didn't sign up for an account, please ignore this email.</p>
                <p class="footer">Best regards,<br>Kyoku</p>
            </div>
        </body>
        </html>
    """.trimIndent()

        sendEmail(
            to = toEmail,
            subject = "Verify Your Email to Complete Registration",
            content = content
        )
    }

    private fun sendEmailLogInVerificationMail(
        toEmail: String,
        verificationMailToken: String,
    ) {
        val verificationLink =
            "${System.getenv("AUTH_URL") + EndPoints.VerifyLogInEmail.route}?token=$verificationMailToken"
        val content = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Log In Verification</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    color: #333;
                    background-color: #f9f9f9;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #fff;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    text-align: center;
                }
                .appLogo img {
                    width: 150px;
                    margin-bottom: 20px;
                    display: block;
                    margin-left: auto;
                    margin-right: auto;
                }
                .header {
                    color: #4CAF50;
                }
                .content {
                    margin: 20px 0;
                }
                .cta-button {
                    display: inline-block;
                    padding: 10px 20px;
                    color: white;
                    background-color: #4CAF50;
                    text-decoration: none;
                    border-radius: 5px;
                    margin-top: 20px;
                }
                .footer {
                    margin-top: 20px;
                    font-size: 0.9em;
                    color: #777;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <p class="appLogo">
                    <a>
                        <img src="${System.getenv("AUTH_URL")}/.well-known/app_logo.png" alt="Kyoku Logo">
                    </a>
                </p>
            
                <h1 class="header">Log In Verification</h1>
                <div class="content">
                    <p>To verify your login, please click the button below:</p>
                    <p>
                        <a href="$verificationLink" class="cta-button">Verify Email</a>
                    </p>
                </div>
                <div class="footer">
                    <p>If you did not attempt to log in, please ignore this email.</p>
                    <p>Best regards,<br>Your Company Team</p>
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()

        sendEmail(
            to = toEmail,
            subject = "Log In Verification",
            content = content
        )
    }

    private fun welcomeMail(
        email: String,
        userName: String,
    ) {
        val developerEmail = System.getenv("email")
        val content = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Welcome to Kyoku</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    color: #333;
                    background-color: #f9f9f9;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #fff;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    text-align: center;
                }
                .header {
                    color: #4CAF50;
                }
                .appLogo img {
                    width: 150px;
                    margin-bottom: 20px;
                    display: block;
                    margin-left: auto;
                    margin-right: auto;
                }
                .content {
                    margin: 20px 0;
                }
                .footer {
                    margin-top: 20px;
                    font-size: 0.9em;
                    color: #777;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <p class="appLogo">
                    <a>
                        <img src="${System.getenv("AUTH_URL")}/.well-known/app_logo.png" alt="Kyoku Logo">
                    </a>
                </p>
                <h1 class="header">Welcome to Kyoku!</h1>
                <div class="content">
                    <p>
                        <h3>Listen to millions of songs ad-free.</h3>
                        <h3>Download as many songs as you want.</h3>
                    </p>
                </div>
                <div class="footer">
                    <p>This app is developed by a single developer.</p>
                    <p>If you encounter any bugs, please email me at: <strong>$developerEmail</strong></p>
                    <p>Hope you have a great experience with Kyoku!</p>
                    <p>Thank you! :)</p>
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()

        sendEmail(
            to = email,
            subject = "Welcome $userName",
            content = content
        )
    }

    private fun welcomeBackMail(
        to: String,
        userName: String,
    ) {
        val content = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>Welcome Back $userName</title>
            <style>
                body {
                    font-family: Arial, sans-serif;
                    color: #333;
                    background-color: #f9f9f9;
                    margin: 0;
                    padding: 20px;
                }
                .container {
                    max-width: 600px;
                    margin: 0 auto;
                    background-color: #fff;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    text-align: center;
                }
                .header {
                    color: #4CAF50;
                }
                .appLogo img {
                    width: 150px;
                    margin-bottom: 20px;
                    display: block;
                    margin-left: auto;
                    margin-right: auto;
                }
                .content {
                    margin: 20px 0;
                }
                .footer {
                    margin-top: 20px;
                    font-size: 0.9em;
                    color: #777;
                }
            </style>
        </head>
        <body>
            <div class="container">
                 <p class="appLogo">
                    <a>
                        <img src="${System.getenv("AUTH_URL")}/.well-known/app_logo.png" alt="Kyoku Logo">
                    </a>
                </p>
                <h1 class="header">Welcome back to Kyoku!</h1>
                <div class="content">
                    <p>It's fantastic to see you back with us!</p>
                    <p>We've missed you and are thrilled to have you back.</p>
                    <p>We hope you're ready for an exciting journey ahead.</p>
                    <p>Here's to making great memories and having a blast together!</p>
                </div>
                <div class="footer">
                    <p>Best regards,</p>
                    <p>From Kyoku :)</p>
                </div>
            </div>
        </body>
        </html>
    """.trimIndent()

        sendEmail(
            to = to,
            subject = "Welcome back $userName",
            content = content
        )
    }

    private fun forgotPasswordMail(
        to: String,
        token: String,
    ) {
        val url = "${System.getenv("AUTH_URL") + EndPoints.ResetPassword.route}?token=$token"

        val content = """
            <!DOCTYPE html>
            <html lang="en">

            <head>
              <meta charset="UTF-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1.0" />
              <title>Password Reset Mail</title>
              <style>
                body {
                  font-family: Arial, sans-serif;
                  color: #333;
                  line-height: 1.6;
                }

                .container {
                  width: 80%;
                  margin: 0 auto;
                  padding: 20px;
                  background-color: #f4f4f4;
                  border-radius: 10px;
                  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                }

                .appLogo img {
                  width: 150px;
                  margin-bottom: 20px;
                  display: block;
                  margin-left: auto;
                  margin-right: auto;
                }

                .header {
                  color: #4caf50;
                  text-align: center;
                }

                .content {
                  margin: 20px 0;
                  text-align: center;
                }

                .cta-button {
                  display: inline-block;
                  padding: 10px 20px;
                  color: white;
                  background-color: #4caf50;
                  text-decoration: none;
                  border-radius: 5px;
                  margin-top: 20px;
                }

                .footer {
                  margin-top: 20px;
                  text-align: center;
                  text-decoration: underline;
                }
              </style>
            </head>

            <body>
              <div class="container">
                <p class="appLogo">
                  <a>
                    <img src="${System.getenv("AUTH_URL")}/.well-known/app_logo.png" alt="Kyoku Logo" />
                  </a>
                </p>

                <h1 class="header">Password reset Mail!</h1>

                <p class="content">You̥ are seeing this email because you requested for a password reset.</p>
                <p class="content">
                  Follow the bellow link to reset you password.
                  Please login again through app after changing you password.
                </p>
                
                <p class="content">
                 This link is only valid for 10 minute.
                </p>
                
                <p class="content">
                  <a href="$url" class="cta-button">Reset Password</a>
                </p>
                <p class="footer">
                  If you didn't requested for password reset, please ignore this email.
                </p>
                <p class="footer">Best regards,<br />Kyoku</p>
              </div>
            </body>

            </html>
        """.trimIndent()

        sendEmail(
            to = to,
            subject = "Password Reset Mail",
            content = content
        )
    }

    private suspend fun getLogInData(
        email: String,
        userType: UserType,
        authKey: String,
    ) {
        val token = jwtRepo.generateToken(
            sub = "GetLogInData",
            email = email,
            claimName = GET_LOGIN_DATA_TOKEN_CLAIM_KEY,
            validationTime = 8000
        )


    }
}