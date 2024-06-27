package com.poulastaa.core.domain

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/auth")

    data object SignUpEmailVerificationCheck :
        EndPoints(route = "/api/auth/signUpEmailVerificationCheck")

    data object LogInEmailVerificationCheck :
        EndPoints(route = "/api/auth/logInEmailVerificationCheck")

    data object ResendVerificationMail : EndPoints(route = "/api/auth/resendVerificationMail")

    data object ForgotPassword : EndPoints(route = "/api/auth/forgotPassword")

    data object RefreshToken : EndPoints(route = "/api/auth/refreshToken")
}