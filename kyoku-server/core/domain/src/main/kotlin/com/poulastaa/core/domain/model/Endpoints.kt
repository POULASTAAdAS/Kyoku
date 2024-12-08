package com.poulastaa.core.domain.model

sealed class Endpoints(val route: String) {
    data object Auth : Endpoints(route = "/api/v1/auth")

    data object VerifyEmail : Endpoints(route = "/api/v1/auth/verify-email")

    data object ForgotPassword : Endpoints(route = "/api/v1/auth/forgot-password")
    data object ResetPassword : Endpoints(route = "/api/v1/auth/reset-password")
    data object SubmitNewPassword : Endpoints(route = "/api/v1/auth/submit-new-password")

    data object UnAuthorized : Endpoints(route = "/api/v1/unauthorized")
}