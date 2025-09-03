package com.poulastaa.kyoku.auth.controller

import com.poulastaa.kyoku.auth.model.Endpoints
import com.poulastaa.kyoku.auth.model.request.EmailSignUp
import com.poulastaa.kyoku.auth.model.request.EmailSingIn
import com.poulastaa.kyoku.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val service: AuthService,
) {
    @PostMapping(Endpoints.EMAIL_SING_IN)
    fun emailLogIn(
        @Valid @RequestBody req: EmailSingIn,
    ) = service.processEmailSingIn(req.email, req.password).toSingInUpResponse()

    @PostMapping(Endpoints.EMAIL_SING_UP)
    fun emailCreateAccount(
        @Valid @RequestBody req: EmailSignUp,
    ) {

    }
}