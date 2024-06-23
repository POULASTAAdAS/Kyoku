package com.poulastaa.core.domain

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/auth")

    data object VerifyEmail : EndPoints(route = "/api/auth/verifyEmail")
    data object EmailVerificationCheck : EndPoints(route = "/api/auth/emailVerificationCheck")
    data object ResendVerificationMail : EndPoints(route = "/api/auth/resendVerificationMail")

    data object ForgotPassword : EndPoints(route = "/api/auth/forgotPassword")

    data object RefreshToken : EndPoints(route = "/api/auth/refreshToken")
}