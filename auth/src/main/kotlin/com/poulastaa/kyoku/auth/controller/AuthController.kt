package com.poulastaa.kyoku.auth.controller

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.poulastaa.kyoku.auth.model.Endpoints
import com.poulastaa.kyoku.auth.model.dto.DtoAuthenticationTokenClaim
import com.poulastaa.kyoku.auth.model.dto.EmailVerificationStatus
import com.poulastaa.kyoku.auth.model.dto.GoogleAuthPayload
import com.poulastaa.kyoku.auth.model.dto.UserType
import com.poulastaa.kyoku.auth.model.request.*
import com.poulastaa.kyoku.auth.model.response.*
import com.poulastaa.kyoku.auth.model.response.ResponseStatus
import com.poulastaa.kyoku.auth.service.AuthService
import com.poulastaa.kyoku.auth.utils.Email
import com.poulastaa.kyoku.auth.utils.JWTToken
import jakarta.validation.Valid
import org.apache.commons.lang.StringEscapeUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException
import java.net.URI
import java.security.GeneralSecurityException

@RestController
class AuthController(
    private val service: AuthService,
    @param:Value("\${jwt.google.clientId}")
    private val clientId: String = "",
    @param:Value("\${jwt.google.issuer}")
    private val issuer: String = "",
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
    ): ResponseEntity<ResponseWrapper<ResponseUser>> {
        // done to prevent XSS(Cross-Site Scripting) injection
        val username = StringEscapeUtils.escapeHtml(req.username) ?: return ResponseEntity.badRequest()
            .body(
                ResponseWrapper<ResponseUser>(
                    status = ResponseStatus.INVALID_REQUEST_BODY,
                    message = ResponseStatus.INVALID_REQUEST_BODY.message,
                    code = HttpStatus.BAD_REQUEST.value(),
                )
            )

        return service.processEmailSingUp(
            username = username,
            email = req.email,
            password = req.password,
            countryCode = req.countryCode,
        ).toSingInUpResponse()
    }

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

        ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
            .location(URI.create("/${Endpoints.STATIC_FILE}?fileName=$fileName"))
            .build<Unit>()
    }

    @GetMapping(Endpoints.CHECK_VERIFICATION_MAIL_STATE)
    fun checkVerificationStatus(
        @Valid @RequestParam email: Email,
        @Valid @RequestParam type: String,
    ): ResponseEntity<ResponseWrapper<ResponseToken>> {
        val userType = try {
            UserType.valueOf(type)
        } catch (_: Exception) {
            return ResponseWrapper<ResponseToken>(status = ResponseStatus.INVALID_REQUEST_BODY).toResponseEntity()
        }

        return service.generateAuthenticationTokens(
            email = email,
            type = userType,
        ).let { token ->
            if (token.isValid()) ResponseWrapper(
                status = ResponseStatus.SUCCESS,
                payload = token,
            ).toResponseEntity() else ResponseWrapper<ResponseToken>(
                status = ResponseStatus.UNAUTHORIZED,
            ).toResponseEntity()
        }
    }

    @PostMapping(Endpoints.REFRESH_TOKEN)
    fun refreshToken(
        @Valid @RequestBody req: RefreshTokenRequest,
    ): ResponseEntity<ResponseWrapper<ResponseToken>> {
        val type = try {
            UserType.valueOf(req.type)
        } catch (_: Exception) {
            return ResponseWrapper<ResponseToken>(status = ResponseStatus.INVALID_REQUEST_BODY).toResponseEntity()
        }

        return service.refreshToken(
            payload = DtoAuthenticationTokenClaim(
                email = req.email,
                userType = type
            ),
            token = req.oldToken
        ).toResponseWrapper().toResponseEntity()
    }

    @GetMapping(Endpoints.FORGOT_PASSWORD)
    fun sendForgotPasswordMail(
        @Valid @RequestParam email: Email,
    ): ResponseEntity<ResponseWrapper<ResponseForgoPasswordMailStatus>> =
        service.sendForgotPasswordMail(email).toResponseEntity()

    @GetMapping(Endpoints.VALIDATE_PASSWORD_OTP)
    fun validateForgotPasswordOTP(
        @Valid @RequestParam code: String,
        @Valid @RequestParam email: Email,
    ): ResponseEntity<ResponseWrapper<CodeValidationResponse>> =
        service.validateForgotPasswordCode(code, email).toResponseWrapper().toResponseEntity()

    @PostMapping(Endpoints.RESET_PASSWORD)
    fun resetPassword(
        @Valid @RequestBody req: UpdatePassword,
    ): ResponseEntity<ResponseWrapper<UpdatePasswordResponse>> =
        service.updatePassword(
            password = req.password,
            token = req.token,
        ).toUpdatePasswordResponseWrapper().toResponseEntity()

    @PostMapping(Endpoints.GOOGLE_AUTH)
    fun googleAuth(
        @Valid @RequestBody req: GoogleAuth,
    ): ResponseEntity<ResponseWrapper<ResponseGoogleAuth>> = req.validateToken()?.let {
        service.processGoogleAuth(
            payload = it,
            countryCode = req.code,
        ).toResponseEntity()
    } ?: ResponseWrapper<ResponseGoogleAuth>( // google token not valid
        status = ResponseStatus.UNAUTHORIZED
    ).toResponseEntity()

    fun GoogleAuth.validateToken() = try {
        GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
            .setAudience(listOf(clientId))
            .setIssuer(issuer)
            .build()
            .verify(this.token)?.let {
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

private fun RefreshTokenResponse.toResponseWrapper() = when (status) {
    RefreshTokenResponseStatus.SUCCESS -> ResponseWrapper(
        status = ResponseStatus.SUCCESS,
        payload = payload,
    )

    RefreshTokenResponseStatus.TOKEN_EXPIRED,
    RefreshTokenResponseStatus.INVALID_TOKEN,
        -> ResponseWrapper(
        status = ResponseStatus.UNAUTHORIZED,
    )
}

private fun CodeValidationResponse.toResponseWrapper() = ResponseWrapper(
    status = when (status) {
        CodeValidationResponseStatus.VALID -> ResponseStatus.SUCCESS
        CodeValidationResponseStatus.USER_NOT_FOUND -> ResponseStatus.USER_NOT_FOUND
        CodeValidationResponseStatus.INVALID_CODE -> ResponseStatus.INVALID_REQUEST_BODY
        CodeValidationResponseStatus.INVALID_EMAIL -> ResponseStatus.EMAIL_NOT_VALID
        CodeValidationResponseStatus.EXPIRED -> ResponseStatus.UNAUTHORIZED
    },
    payload = this,
)

private fun ResponseWrapper<UpdatePasswordResponse>.toUpdatePasswordResponseWrapper(): ResponseWrapper<UpdatePasswordResponse> {
    val updatePasswordStatus = payload?.status

    return copy(
        status = when (updatePasswordStatus) {
            UpdatePasswordStatus.UPDATED -> ResponseStatus.SUCCESS
            UpdatePasswordStatus.USER_NOT_FOUND -> ResponseStatus.USER_NOT_FOUND
            UpdatePasswordStatus.SAME_PASSWORD,
            UpdatePasswordStatus.INVALID_PASSWORD,
                -> ResponseStatus.INVALID_PASSWORD

            UpdatePasswordStatus.EXPIRED_TOKEN,
            UpdatePasswordStatus.INVALID_TOKEN,
                -> ResponseStatus.UNAUTHORIZED

            UpdatePasswordStatus.ERROR,
            null,
                -> ResponseStatus.INTERNAL_SERVER_ERROR
        },
    )
}

private fun ResponseToken.isValid() = accessToken.isNotBlank() && refreshToken.isNotBlank()
