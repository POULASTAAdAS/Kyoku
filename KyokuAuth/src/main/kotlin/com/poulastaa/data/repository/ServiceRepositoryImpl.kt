package com.poulastaa.data.repository

import com.poulastaa.data.dao.other.Country
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.req.EmailLogInReq
import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.res.Payload
import com.poulastaa.data.model.auth.response.EmailAuthRes
import com.poulastaa.data.model.auth.response.GoogleAuthRes
import com.poulastaa.data.model.auth.response.UserAuthStatus
import com.poulastaa.domain.repository.AuthRepository
import com.poulastaa.domain.repository.JWTRepository
import com.poulastaa.domain.repository.ServiceRepository
import com.poulastaa.domain.repository.UserId
import com.poulastaa.domain.table.other.CountryTable
import com.poulastaa.plugins.dbQuery
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

        val response = authRepo.loginEmailUser(
            email = req.email,
            password = req.password,
            refreshToken = refreshToken
        )

        when (response.status) {
            UserAuthStatus.USER_FOUND_HOME -> Unit
            UserAuthStatus.USER_FOUND_STORE_B_DATE -> Unit
            UserAuthStatus.USER_FOUND_SET_GENRE -> Unit
            UserAuthStatus.USER_FOUND_SET_ARTIST -> Unit

            UserAuthStatus.EMAIL_NOT_VERIFIED -> {
                val verificationMailToken = jwtRepo.generateVerificationMailToken(email = req.email)

                sendEmailSignUpVerificationMail(
                    toEmail = req.email,
                    verificationMailToken = verificationMailToken,
                )

                return response
            }

            else -> return response
        }.let {
            val verificationMailToken = jwtRepo.generateVerificationMailToken(email = req.email)

            sendEmailLogInVerificationMail(
                toEmail = req.email,
                verificationMailToken = verificationMailToken,
            )
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
                // todo welcome mail

                response.response to response.userId
            }

            UserAuthStatus.USER_FOUND_STORE_B_DATE -> response.response to response.userId
            UserAuthStatus.USER_FOUND_SET_GENRE -> response.response to response.userId
            UserAuthStatus.USER_FOUND_SET_ARTIST -> response.response to response.userId

            UserAuthStatus.USER_FOUND_HOME -> {
                // todo welcome back mail

                response.response to response.userId
            }

            else -> GoogleAuthRes() to -1
        }
    }

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
    }
}