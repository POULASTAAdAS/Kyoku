package com.poulastaa.kyoku.auth.controller

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.poulastaa.kyoku.auth.model.Endpoints
import com.poulastaa.kyoku.auth.model.dto.*
import com.poulastaa.kyoku.auth.model.request.EmailSignUp
import com.poulastaa.kyoku.auth.model.request.EmailSingIn
import com.poulastaa.kyoku.auth.model.request.GoogleAuth
import com.poulastaa.kyoku.auth.model.request.RefreshTokenRequest
import com.poulastaa.kyoku.auth.model.response.RefreshTokenResponse
import com.poulastaa.kyoku.auth.model.response.ResponseToken
import com.poulastaa.kyoku.auth.model.response.ResponseWrapper
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.service.AuthService
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.JWTToken
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.net.URI
import java.security.GeneralSecurityException

@RestController
class AuthController(
    private val service: AuthService,
    private val client: LoadBalancerClient,
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
    ) = service.validateAuthenticationMailPayload(token).let { result ->
        val fileName =
            when (result) { // don't change file names as it must be same char by char to file-server file name
                EmailVerificationStatus.VALID -> "EmailVerificationSuccess.html"
                EmailVerificationStatus.TOKEN_ALREADY_USED -> "LinkAlreadyUsed.html"
                EmailVerificationStatus.TOKEN_EXPIRED -> "LinkExpired.html"
                EmailVerificationStatus.USER_NOT_FOUND -> "UserNotFound.html"
            }

        // TODO: IMPROVEMENT replace with grpc if needed
        val uri = URI.create("${client.choose("file").uri}/${Endpoints.STATIC_FILE}?fileName=$fileName")
        ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
            .location(uri)
            .build<Any>()
    }

    @GetMapping(Endpoints.CHECK_VERIFICATION_MAIL_STATE)
    fun checkVerificationStatus(
        @Valid @RequestParam email: Email,
        @Valid @RequestParam type: String,
    ): ResponseEntity<ResponseToken> {
        val type = try {
            UserType.valueOf(type)
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseToken())
        }

        return ResponseEntity.ok(
            service.generateAuthenticationTokens(
                email = email,
                type = type,
            )
        )
    }

    @PostMapping(Endpoints.REFRESH_TOKEN)
    fun refreshToken(
        @Valid @RequestBody req: RefreshTokenRequest,
    ): ResponseEntity<RefreshTokenResponse> {
        val type = try {
            UserType.valueOf(req.type)
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(RefreshTokenResponse())
        }

        return service.refreshToken(
            payload = DtoAuthenticationTokenClaim(
                email = req.email,
                userType = type
            ),
            token = req.oldToken
        ).let {
            ResponseEntity.status(it.status.status).body(it)
        }
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