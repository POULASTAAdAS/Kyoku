package com.poulastaa.kyoku.auth.controller

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.poulastaa.kyoku.auth.model.Endpoints
import com.poulastaa.kyoku.auth.model.dto.DtoEmailVerificationStatus
import com.poulastaa.kyoku.auth.model.dto.DtoUser
import com.poulastaa.kyoku.auth.model.dto.GoogleAuthPayload
import com.poulastaa.kyoku.auth.model.request.EmailSignUp
import com.poulastaa.kyoku.auth.model.request.EmailSingIn
import com.poulastaa.kyoku.auth.model.request.GoogleAuth
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.service.AuthService
import com.poulastaa.kyoku.auth.utils.JWTToken
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.security.GeneralSecurityException

@RestController
class AuthController(
    private val service: AuthService,
) {
    @PostMapping(Endpoints.EMAIL_SING_IN)
    fun emailLogIn(
        @Valid @RequestBody req: EmailSingIn,
    ) = service.processEmailSingIn(
        req.email,
        req.password
    ).toSingInUpResponse()

    @PostMapping(Endpoints.EMAIL_SING_UP)
    fun emailCreateAccount(
        @Valid @RequestBody req: EmailSignUp,
    ) = service.processEmailSingUp(
        username = req.username,
        email = req.email,
        password = req.password,
        countryCode = req.countryCode,
    ).toSingInUpResponse()

    @PostMapping(Endpoints.GOOGLE_AUTH)
    fun googleAuth(
        @Valid @RequestBody req: GoogleAuth,
    ) = req.validateToken()?.let {
        service.processGoogleAuth(
            payload = it,
            countryCode = req.code,
        ).toSingInUpResponse()
    } ?: ResponseWrapper<DtoUser>( // google token not valid
        status = ResponseStatus.INTERNAL_SERVER_ERROR
    ).toSingInUpResponse()

    @GetMapping(Endpoints.VERIFY_EMAIL)
    fun validateAuthenticationMail(
        @Valid @RequestParam token: JWTToken,
    ) = when (service.validateAuthenticationMailPayload(token)) {
        DtoEmailVerificationStatus.VALID -> TODO()
        DtoEmailVerificationStatus.TOKEN_ALREADY_USED -> TODO()
        DtoEmailVerificationStatus.TOKEN_EXPIRED -> TODO()
        DtoEmailVerificationStatus.TOKEN_INVALID -> TODO()
        DtoEmailVerificationStatus.USER_NOT_FOUND -> TODO()
        DtoEmailVerificationStatus.SERVER_ERROR -> TODO()
    }

    fun GoogleAuth.validateToken(
        @Value("\${jwt.google.secret}")
        clientId: String = "",
        @Value("\${jwt.google.issuer}")
        issuer: String = "",
    ) = try {
        GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(clientId))
            .setIssuer(issuer)
            .build()
            .verify(this.token)?.let {
                println()
                println(it.payload)
                println()
                println()
                println(it.payload)
                println()
                println()

                GoogleAuthPayload(
                    sub = it.payload.subject,
                    name = it.payload["name"] as? String ?: return@let null,
                    displayName = it.payload["name"] as? String ?: return@let null,
                    email = it.payload.email,
                    picture = it.payload["picture"] as? String ?: return@let null,
                )
            }
    } catch (_: GeneralSecurityException) {
        null
    } catch (_: IOException) {
        null
    } catch (_: Exception) {
        null
    }
}