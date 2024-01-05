package com.example.routes.auth.common

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.data.model.EmailSignUpReq
import com.example.data.model.EndPoints
import com.example.data.model.VerificationMailApiResponse
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.util.Constants.BASE_URL
import com.example.util.Constants.GOOGLE_SMTP_HOST
import com.sun.mail.util.MailConnectException
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import kotlinx.serialization.json.Json
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.set


suspend fun PipelineContext<Unit, ApplicationCall>.handleEmailSignup(
    emailSignUpReq: EmailSignUpReq,
    emailAuthUser: EmailAuthUserRepository,
) {
    if (false) // reduce unnecessary api call while developing
        verifyEmail(
            email = emailSignUpReq.email,
        ).let {
            if (!it) {
                call.respond(
                    message = "not a valid email",
                    status = HttpStatusCode.BadRequest
                )
                return
            }
        }

    when (
        createUser(
            emailSignUpReq.userName,
            emailSignUpReq.email,
            emailSignUpReq.password,
            emailAuthUser = emailAuthUser
        )
    ) {
        UserCreationStatus.CREATED -> {
            call.respond(
                message = "user created",
                status = HttpStatusCode.OK
            )
        }

        UserCreationStatus.CONFLICT -> {
            call.respond(
                message = "user already exists",
                status = HttpStatusCode.Conflict
            )
            return
        }

        UserCreationStatus.INTERNAL_SERVER_ERROR -> {
            call.respond(
                message = "something went wrong",
                status = HttpStatusCode.InternalServerError
            )
            return
        }
    }

    if (!sendAuthenticationMail(emailSignUpReq.email, call)) {
        call.respond(
            message = "something went wrong sending authentication mail",
            status = HttpStatusCode.InternalServerError
        )
    }
}

private suspend fun verifyEmail(
    email: String,
): Boolean {
    val client = HttpClient()

    val responseBody = client.get("https://email-checker.p.rapidapi.com/verify/v1?email=${email}") {
        headers {
            append("X-RapidAPI-Key", System.getenv("emailVerifierKey"))
            append("X-RapidAPI-Host", "email-checker.p.rapidapi.com")
        }
    }.bodyAsText()

    client.close()
    return try {
        Json.decodeFromString<VerificationMailApiResponse>(responseBody).status.lowercase() == "valid"
    } catch (e: Exception) {
        false
    }
}

private suspend fun createUser(
    userName: String,
    email: String,
    password: String,
    emailAuthUser: EmailAuthUserRepository,
): UserCreationStatus {
    return emailAuthUser.createUser(
        userName = userName,
        email = email,
        password = password
    )
}


private fun sendAuthenticationMail(
    to: String,
    call: ApplicationCall
): Boolean {
    val props = Properties().apply {
        this["mail.smtp.host"] = GOOGLE_SMTP_HOST
        this["mail.smtp.port"] = "587"
        this["mail.smtp.auth"] = "true"
        this["mail.smtp.starttls.enable"] = "true"
    }


    val session = Session.getInstance(
        props,
        object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(
                    System.getenv("email"),
                    System.getenv("password")
                )
            }
        }
    )

    return try {
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(System.getenv("email")))
        message.setRecipients(
            Message.RecipientType.TO,
            to
        )

        message.subject = "Authentication Mail"
        message.sentDate = Date()

        val token = generateVerificationMailToken(call, email = to)

        val htmlContent = (
                (
                        "<html>"
                                + "<body>"
                                + "<h1>Email Authentication</h1>"
                                + "<p>Click the following link to authenticate your email:</p>"
                                + "<a href=\"${BASE_URL + EndPoints.VerifyEmail.route}?token=" + token) + "\">Authenticate</a>"
                        + "</body>"
                        + "</html>"
                )

        message.setContent(htmlContent, "text/html")
        Transport.send(message)
        true
    } catch (e: MessagingException) {
        false
    } catch (e: MailConnectException) {
        false
    } catch (e: java.net.ConnectException) {
        false
    } catch (e: Exception) {
        false
    }
}

private fun generateVerificationMailToken(
    call: ApplicationCall,
    email: String
): String {
    val issuer = call.application.environment.config.property("jwt.issuer").getString()

    val privateKeyString = call.application.environment.config.property("jwt.privateKey").getString()
    val audience = call.application.environment.config.property("jwt.audience").getString()

    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(1, 5, TimeUnit.MINUTES)
        .rateLimited(3, 1, TimeUnit.MINUTES)
        .build()

    val publicKey = jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey

    val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
    val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)

    return JWT.create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withClaim("email", email)
        .withExpiresAt(Date(System.currentTimeMillis() + 300000L))
        .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
        ?: throw Exception("unable to create jwt token")
}