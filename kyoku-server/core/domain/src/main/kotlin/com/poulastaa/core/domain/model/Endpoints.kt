package com.poulastaa.core.domain.model

sealed class Endpoints(val route: String) {
    data object Auth : Endpoints(route = "/api/v1/auth")

    data object VerifyEmail : Endpoints(route = "/api/v1/auth/verifyEmail")
    data object VerifyEmailVerificationState : Endpoints(route = "/api/v1/auth/verifyEmailVerificationState")

    data object ForgotPassword : Endpoints(route = "/api/v1/auth/forgotPassword")
    data object ResetPassword : Endpoints(route = "/api/v1/auth/resetPassword")
    data object SubmitNewPassword : Endpoints(route = "/api/v1/auth/submitNewPassword")

    data object UnAuthorized : Endpoints(route = "/api/v1/unauthorized")
}