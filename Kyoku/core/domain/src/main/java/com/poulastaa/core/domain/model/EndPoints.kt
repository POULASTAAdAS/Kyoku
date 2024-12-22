package com.poulastaa.core.domain.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/v1/auth")

    data object VerifyEmail : EndPoints(route = "/api/v1/auth/verifyEmail")
    data object GetJWTToken : EndPoints(route = "/api/v1/auth/getJWTToken")
    data object GenerateRefreshToken : EndPoints(route = "/api/v1/auth/generateRefreshToken")

    data object ForgotPassword : EndPoints(route = "/api/v1/auth/forgotPassword")
}