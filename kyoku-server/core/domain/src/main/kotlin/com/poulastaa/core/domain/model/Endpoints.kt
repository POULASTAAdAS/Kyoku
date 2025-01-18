package com.poulastaa.core.domain.model

sealed class Endpoints(val route: String) {
    data object Auth : Endpoints(route = "/api/v1/auth")

    data object VerifyEmail : Endpoints(route = "/api/v1/auth/verifyEmail")
    data object GetJWTToken : Endpoints(route = "/api/v1/auth/getJWTToken")

    data object ForgotPassword : Endpoints(route = "/api/v1/auth/forgotPassword")
    data object ResetPassword : Endpoints(route = "/api/v1/auth/resetPassword")
    data object SubmitNewPassword : Endpoints(route = "/api/v1/auth/submitNewPassword")

    data object ImportSpotifyPlaylist : Endpoints(route = "/api/v1/user/importSpotifyPlaylist")

    data object Poster : Endpoints(route = "/api/v1/user/poster")

    data object SetBDate : Endpoints(route = "/api/v1/user/setBDate")

    data object UnAuthorized : Endpoints(route = "/api/v1/unauthorized")
}