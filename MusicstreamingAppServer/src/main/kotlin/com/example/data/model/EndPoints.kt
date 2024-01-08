package com.example.data.model

sealed class EndPoints(val route: String) {
    data object Auth : EndPoints(route = "/api/auth")

    data object VerifyEmail : EndPoints(route = "/api/auth/verifyEmail")
    data object EmailVerificationCheck : EndPoints(route = "/api/auth/emailVerificationCheck")
    data object ForgotPassword : EndPoints(route = "/api/auth/forgotPassword")
    data object ResetPassword : EndPoints(route = "/api/auth/resetPassword")

    data object ProfilePic : EndPoints(route = "/api/authorised/user/profilePic")
    data object GetSpotifyPlaylist : EndPoints(route = "/api/authorised/spotifyPlaylist")

    data object PlaySong : EndPoints(route = "/api/authorised/playSong")
    data object CoverImage : EndPoints(route = "/api/authorised/coverImage")

    data object UnAuthorised : EndPoints(route = "/api/unauthorised")

}